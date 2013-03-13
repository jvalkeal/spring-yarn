package org.springframework.yarn.batch.repository;

import java.rmi.RemoteException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.util.StringUtils;
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Base class for all daos handling remote calls through
 * {@link AppmasterScOperations}.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractRemoteDao {

    private AppmasterScOperations appmasterScOperations;

    /**
     * Default constructor. {@link AppmasterScOperations} should be
     * set via {@link #setAppmasterScOperations(AppmasterScOperations)}
     * method.
     */
    public AbstractRemoteDao() {
    }

    /**
     * Constructor which sets the {@link AppmasterScOperations}.
     * 
     * @param appmasterScOperations {@link AppmasterScOperations} to set
     */
    public AbstractRemoteDao(AppmasterScOperations appmasterScOperations) {
        this.appmasterScOperations = appmasterScOperations;
    }
    
    /**
     * Gets the {@link AppmasterScOperations} for this implementation.
     * 
     * @return {@link AppmasterScOperations} used for this implementation
     */
    public AppmasterScOperations getAppmasterScOperations() {
        return appmasterScOperations;
    }

    /**
     * Sets the {@link AppmasterScOperations} for this implementation.
     * 
     * @param appmasterScOperations the {@link AppmasterScOperations}
     */
    public void setAppmasterScOperations(AppmasterScOperations appmasterScOperations) {
        this.appmasterScOperations = appmasterScOperations;
    }
    
    /**
     * Checks status of a response and throws an exception if response status
     * message is set to 'error'.
     * 
     * @param responseObject the response object
     * @throws RemoteException if state is set to error
     */
    protected void checkResponseMayThrow(BaseResponseObject responseObject) throws RemoteException {
        if(responseObject.getResstate() != null && responseObject.getResstate().contains("error")) {
            if(StringUtils.hasText(responseObject.resmsg)) {
                throw new RemoteException(responseObject.resmsg);                                
            } else {
                throw new RemoteException();                
            }
        }
    }
    
    /**
     * Converts given exception to Spring dao exception.
     * 
     * @param e the exception
     * @return Converted exception
     */
    protected DataAccessException convertException(Exception e) {
        // TODO: do better mapping for exceptions
        return new DataRetrievalFailureException(e.getMessage(), e);
    }

}
