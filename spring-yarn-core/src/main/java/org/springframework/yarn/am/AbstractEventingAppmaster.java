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
