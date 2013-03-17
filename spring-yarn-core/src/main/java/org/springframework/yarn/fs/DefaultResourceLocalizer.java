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
import org.springframework.yarn.fs.LocalResourcesFactoryBean.Entry;

/**
 * Default implementation of {@link ResourceLocalizer} which
 * is capable of distributing files into hdfs and preparing
 * correct parameters for created {@link LocalResource} entries.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultResourceLocalizer implements ResourceLocalizer {

	private final static Log log = LogFactory.getLog(DefaultResourceLocalizer.class);

	private final Collection<Entry> transferEntries;
	private final Configuration configuration;
	private Map<String, LocalResource> resources;
	private boolean distributed = false;
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
					Path localPath = new Path(e.local + e.path);
					Path remotePath = new Path(e.remote + e.path);

					// //hdfs://0.0.0.0.0:9000/user/janne/tmp/foo.jar
					// //hdfs://192.168.223.139:9000/user/janne/tmp/foo.jar
					// fs.copyFromLocalFile(false, true, src, dst);
					FileStatus destStatus = fs.getFileStatus(remotePath);
					LocalResource res = Records.newRecord(LocalResource.class);
					res.setType(e.type);
					res.setVisibility(e.visibility);
					// res.setResource(ConverterUtils.getYarnUrlFromPath(dst));
					res.setResource(ConverterUtils.getYarnUrlFromPath(remotePath));
					res.setTimestamp(destStatus.getModificationTime());
					res.setSize(destStatus.getLen());
					resources.put(localPath.getName(), res);
					distributed = true;
				}
			}
		} catch (IOException e) {
			// TODO: we should probably throw something. it's
			//       unlikely that anything works if we failed
			log.error("Error distributing files", e);
		} finally {
			distributeLock.unlock();
		}
	}

}
