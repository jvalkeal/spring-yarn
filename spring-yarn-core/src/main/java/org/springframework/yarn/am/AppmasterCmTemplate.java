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
package org.springframework.yarn.am;

import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.yarn.api.ContainerManager;
import org.apache.hadoop.yarn.api.protocolrecords.StartContainerRequest;
import org.apache.hadoop.yarn.api.protocolrecords.StartContainerResponse;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.exceptions.YarnRemoteException;
import org.springframework.yarn.rpc.YarnRpcAccessor;
import org.springframework.yarn.rpc.YarnRpcCallback;

public class AppmasterCmTemplate extends YarnRpcAccessor<ContainerManager> implements AppmasterCmOperations {

	private Container container;

	public AppmasterCmTemplate(Configuration config, Container container) {
		super(ContainerManager.class, config);
		this.container = container;
	}

	@Override
	public StartContainerResponse startContainer(final StartContainerRequest request) {
		return execute(new YarnRpcCallback<StartContainerResponse, ContainerManager>() {
			@Override
			public StartContainerResponse doInYarn(ContainerManager proxy) throws YarnRemoteException {
				return proxy.startContainer(request);
			}
		});
	}

	@Override
	protected InetSocketAddress getRpcAddress(Configuration config) {
		String cmIpPortStr = container.getNodeId().getHost() + ":" + container.getNodeId().getPort();
		return NetUtils.createSocketAddr(cmIpPortStr);
	}

}
