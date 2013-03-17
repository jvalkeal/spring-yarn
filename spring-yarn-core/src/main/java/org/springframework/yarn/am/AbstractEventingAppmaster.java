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

/**
 * Base implementation of application master where life-cycle
 * is based on events rather than a static information existing
 * prior the start of an instance.
 * <p>
 * Life-cycle of this instance is not bound to information or
 * states existing prior the startup of an application master.
 * Containers can be requested and launched on demand and application
 * master is then responsible to know when it's time to bail out
 * and end the application.
 * <p>
 * Due to complex need of event communication, the actual event system
 * is abstracted order to plug different systems for a need of
 * various use cases.
 *
 * @author Janne Valkealahti
 *
 */
public class AbstractEventingAppmaster extends AbstractAppmaster {

}
