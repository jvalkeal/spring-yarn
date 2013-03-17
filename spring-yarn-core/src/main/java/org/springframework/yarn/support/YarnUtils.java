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
package org.springframework.yarn.support;

import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.yarn.YarnException;
import org.apache.hadoop.yarn.exceptions.YarnRemoteException;
import org.springframework.dao.DataAccessException;
import org.springframework.yarn.YarnSystemException;

/**
 * Different utilities.
 *
 * @author Janne Valkealahti
 *
 */
public class YarnUtils {

	/**
	 * Converts {@link YarnRemoteException} to a Spring dao exception.
	 *
	 * @param e the {@link YarnRemoteException}
	 * @return a wrapped native exception into {@link DataAccessException}
	 */
	public static DataAccessException convertYarnAccessException(YarnRemoteException e) {
		return new YarnSystemException(e);
	}

	/**
	 * Converts {@link RemoteException} to a Spring dao exception.
	 *
	 * @param e the {@link RemoteException}
	 * @return a wrapped native exception into {@link DataAccessException}
	 */
	public static DataAccessException convertYarnAccessException(RemoteException e) {
		return new YarnSystemException(e);
	}

	/**
	 * Converts {@link YarnException} to a Spring dao exception.
	 *
	 * @param e the {@link YarnException}
	 * @return a wrapped native exception into {@link DataAccessException}
	 */
	public static DataAccessException convertYarnAccessException(YarnException e) {
		return new YarnSystemException(e);
	}

}
