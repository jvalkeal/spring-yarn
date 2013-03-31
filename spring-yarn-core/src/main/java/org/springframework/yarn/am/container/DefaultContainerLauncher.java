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
package org.springframework.yarn.am.container;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.yarn.api.protocolrecords.StartContainerRequest;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.util.Records;

/**
 * Default container launcher.
 *
 * @author Janne Valkealahti
 *
 */
public class DefaultContainerLauncher extends AbstractLauncher implements ContainerLauncher {

	@Override
	public void launchContainer(Container container, List<String> commands) {
		ContainerLaunchContext ctx = Records.newRecord(ContainerLaunchContext.class);
		ctx.setContainerId(container.getId());
		ctx.setResource(container.getResource());
		ctx.setUser(getUsername());
		ctx.setLocalResources(getResourceLocalizer().getResources());
		ctx.setCommands(commands);
		ctx.setEnvironment(getEnvironment());
		ctx = getInterceptors().preLaunch(ctx);

		try {
			ByteBuffer credentialsBuffer = ByteBuffer.wrap(new byte[]{});
			Credentials credentials = new Credentials();

			credentials.addSecretKey(new Text("syarn.secret"), "foo".getBytes());

			DataOutputBuffer containerTokens_dob = new DataOutputBuffer();
			credentials.writeTokenStorageToStream(containerTokens_dob);
			credentialsBuffer =
					ByteBuffer.wrap(containerTokens_dob.getData(), 0,
						containerTokens_dob.getLength());

			ctx.setContainerTokens(credentialsBuffer);

		} catch (IOException e) {
			e.printStackTrace();
		}

		StartContainerRequest request = Records.newRecord(StartContainerRequest.class);
		request.setContainerLaunchContext(ctx);
		getCmTemplate(container).startContainer(request);

		if(getYarnEventPublisher() != null) {
			getYarnEventPublisher().publishContainerLaunched(this, container);
		}

	}

}
