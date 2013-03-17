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
package org.springframework.yarn.rpc;

import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.yarn.YarnException;
import org.apache.hadoop.yarn.exceptions.YarnRemoteException;
import org.apache.hadoop.yarn.ipc.YarnRPC;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.yarn.support.YarnUtils;

/**
 * Base implementation for accessing yarn components over
 * protocol buffer rpc system.
 *
 * @author Janne Valkealahti
 *
 * @param <P> Type of the protocol buffer implementation
 */
public abstract class YarnRpcAccessor<P> implements InitializingBean, DisposableBean {

	private Class<P> protocolClazz;
	private Configuration configuration;
	private InetSocketAddress address;
	private P proxy;

	public YarnRpcAccessor(Class<P> protocolClazz, Configuration config) {
		this.protocolClazz = protocolClazz;
		this.configuration = config;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		address = getRpcAddress(configuration);
		proxy = createProxy();
	}

	@Override
	public void destroy() {
		RPC.stopProxy(proxy);
	}

	public P getProxy() {
		return proxy;
	}

	public <T, S extends P> T execute(YarnRpcCallback<T, S> action) throws DataAccessException {
		@SuppressWarnings("unchecked")
		S proxy = (S) getProxy();
		try {
			T result = action.doInYarn(proxy);
			return result;
		} catch (YarnException e) {
			throw YarnUtils.convertYarnAccessException(e);
		} catch (YarnRemoteException e) {
			throw YarnUtils.convertYarnAccessException(e);
		} catch (RemoteException e) {
			throw YarnUtils.convertYarnAccessException(e);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	protected P createProxy() {
		YarnRPC rpc = YarnRPC.create(configuration);
		return (P) rpc.getProxy(protocolClazz, address, configuration);
	}

	/**
	 * Gets the {@link InetSocketAddress} where this accessor should connect.
	 *
	 * @param configuration the yarn configuration
	 * @return address of rpc endpoint
	 */
	protected abstract InetSocketAddress getRpcAddress(Configuration configuration);

}
