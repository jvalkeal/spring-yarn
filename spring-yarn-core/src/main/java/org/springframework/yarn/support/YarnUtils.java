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
