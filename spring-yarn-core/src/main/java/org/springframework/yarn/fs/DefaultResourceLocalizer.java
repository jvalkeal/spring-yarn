/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.yarn.fs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;
import org.springframework.util.ObjectUtils;
import org.springframework.yarn.YarnSystemException;
import org.springframework.yarn.fs.LocalResourcesFactoryBean.Entry;

/**
 * Default implementation of {@link ResourceLocalizer} which
 * is only capable of re-using files already in HDFS and preparing
 * correct parameters for created {@link LocalResource} entries.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultResourceLocalizer implements ResourceLocalizer {

	private final static Log log = LogFactory.getLog(DefaultResourceLocalizer.class);

	/** Raw resource entries. */
	private final Collection<Entry> transferEntries;

	/** Yarn configuration, needed to access the hdfs */
	private final Configuration configuration;

	/** Map returned from this instance */
	private Map<String, LocalResource> resources;

	/** Flag if distribution work is done */
	private boolean distributed = false;

	/** Locking the work*/
	private final ReentrantLock distributeLock = new ReentrantLock();

	public DefaultResourceLocalizer(Configuration configuration, Collection<Entry> transferEntries) {
		this.configuration = configuration;
		this.transferEntries = transferEntries;
	}

	@Override
	public Map<String, LocalResource> getResources() {
		if (!distributed) {
			distribute();
		}
		return resources;
	}

	@Override
	public void distribute() {
		// guard by lock to distribute only once
		distributeLock.lock();
		try {
			if (!distributed) {
				resources = new HashMap<String, LocalResource>();
				FileSystem fs = FileSystem.get(configuration);
				for (Entry e : transferEntries) {
					Path remotePath = new Path(e.remote + e.path);
					URI localUri = new URI(e.local);
					FileStatus[] fileStatuses = fs.globStatus(remotePath);
					if (!ObjectUtils.isEmpty(fileStatuses)) {
						for(FileStatus status : fileStatuses) {
							if(status.isFile()) {
								URI remoteUri = status.getPath().toUri();
								Path path = new Path(new Path(localUri), remoteUri.getPath());
								LocalResource res = Records.newRecord(LocalResource.class);
								res.setType(e.type);
								res.setVisibility(e.visibility);
								res.setResource(ConverterUtils.getYarnUrlFromPath(path));
								res.setTimestamp(status.getModificationTime());
								res.setSize(status.getLen());
								if(log.isDebugEnabled()) {
									log.debug("Using remote uri [" + remoteUri + "] and local uri [" +
											localUri + "] converted to path [" + path + "]");
								}
								resources.put(status.getPath().getName(), res);
							}
						}
					}
					distributed = true;
				}
			}
		} catch (IOException e) {
			log.error("Error distributing files", e);
			throw new YarnSystemException("Unable to distribute files", e);
		} catch (URISyntaxException e1) {
			log.error("Error distributing files", e1);
			throw new YarnSystemException("Unable to distribute files", e1);
		} finally {
			distributeLock.unlock();
		}
	}

}
