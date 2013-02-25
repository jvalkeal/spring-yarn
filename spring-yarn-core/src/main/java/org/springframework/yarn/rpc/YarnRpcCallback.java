package org.springframework.yarn.rpc;

import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.yarn.exceptions.YarnRemoteException;

/**
 * Simple helper interface to execute methods via callbacks.
 * 
 * @author Janne Valkealahti
 * 
 * @param <T> Type of the return value
 * @param <P> Type of the rpc protocol
 */
public interface YarnRpcCallback<T, P> {

    /**
     * Execute callback.
     * 
     * @param proxy rpc proxy instance
     * @return Value returned by callback
     * @throws YarnRemoteException
     * @throws RemoteException
     */
    T doInYarn(P proxy) throws YarnRemoteException, RemoteException;

}
