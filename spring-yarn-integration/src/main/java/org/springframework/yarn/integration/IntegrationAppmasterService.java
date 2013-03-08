package org.springframework.yarn.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.yarn.am.AppmasterService;
import org.springframework.yarn.integration.support.PortExposingTcpSocketSupport;
import org.springframework.yarn.support.LifecycleObjectSupport;

/**
 * Base implementation of {@link AppmasterService} based on communication
 * link build on top of Spring Integration Ip channels.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class IntegrationAppmasterService extends LifecycleObjectSupport implements AppmasterService {

    private static final Log log = LogFactory.getLog(IntegrationAppmasterService.class);

    private PortExposingTcpSocketSupport socketSupport;

    @Override
    protected void doStart() {
    }

    @Override
    protected void doStop() {
    }

    @Override
    public int getPort() {
        return socketSupport.getServerSocketPort();
    }

    @Override
    public String getHost() {
        return socketSupport.getServerSocketAddress();
    }

    public void setSocketSupport(PortExposingTcpSocketSupport socketSupport) {
        Assert.notNull(socketSupport, "socketSupport must not be null");
        this.socketSupport = socketSupport;
        if(log.isDebugEnabled()) {
            log.debug("Setting socket support: " + socketSupport);
        }
    }

}
