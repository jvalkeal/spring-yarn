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
