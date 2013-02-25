package org.springframework.yarn;

import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.yarn.YarnException;
import org.apache.hadoop.yarn.exceptions.YarnRemoteException;
import org.springframework.dao.UncategorizedDataAccessException;

/**
 * General exception indicating a problem in components interacting with yarn.
 * Main point of wrapping native yarn exceptions inside this is to have common
 * Spring dao exception hierarchy.
 * 
 * @author Janne Valkealahti
 * 
 */
public class YarnSystemException extends UncategorizedDataAccessException {

    private static final long serialVersionUID = -280113474245028099L;

    /**
     * Constructs YarnSystemException from {@link YarnException}.
     * 
     * @param e the {@link YarnException}
     */
    public YarnSystemException(YarnException e) {
        super(e.getMessage(), e);
    }

    /**
     * Constructs YarnSystemException from {@link YarnRemoteException}.
     * 
     * @param e the {@link YarnRemoteException}
     */
    public YarnSystemException(YarnRemoteException e) {
        super(e.getMessage(), e);
    }

    /**
     * Constructs YarnSystemException from {@link RemoteException}.
     * 
     * @param e the {@link RemoteException}
     */
    public YarnSystemException(RemoteException e) {
        super(e.getMessage(), e);
    }

    /**
     * Constructs a general YarnSystemException.
     * 
     * @param message the message
     * @param e the exception
     */
    public YarnSystemException(String message, Exception e) {
        super(message, e);
    }

}
