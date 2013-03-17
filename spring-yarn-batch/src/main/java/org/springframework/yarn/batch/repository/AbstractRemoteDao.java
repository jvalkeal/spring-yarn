package org.springframework.yarn.batch.repository;

import java.rmi.RemoteException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.util.StringUtils;
import org.springframework.yarn.client.AppmasterScOperations;
import org.springframework.yarn.integration.ip.mind.AppmasterMindScOperations;
import org.springframework.yarn.integration.ip.mind.binding.BaseResponseObject;

/**
 * Base class for all daos handling remote calls through
 * {@link AppmasterScOperations}.
 * 
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractRemoteDao {

    private AppmasterMindScOperations appmasterScOperations;

    /**
     * Default constructor. {@link AppmasterMindScOperations} should be
     * set via {@link #setAppmasterScOperations(AppmasterMindScOperations)}
     * method.
     */
    public AbstractRemoteDao() {
    }

    /**
     * Constructor which sets the {@link AppmasterMindScOperations}.
     * 
     * @param appmasterScOperations {@link AppmasterMindScOperations} to set
     */
    public AbstractRemoteDao(AppmasterMindScOperations appmasterScOperations) {
        this.appmasterScOperations = appmasterScOperations;
    }
    
    /**
     * Gets the {@link AppmasterMindScOperations} for this implementation.
     * 
     * @return {@link AppmasterMindScOperations} used for this implementation
     */
    public AppmasterMindScOperations getAppmasterScOperations() {
        return appmasterScOperations;
    }

    /**
     * Sets the {@link AppmasterMindScOperations} for this implementation.
     * 
     * @param appmasterScOperations the {@link AppmasterMindScOperations}
     */
    public void setAppmasterScOperations(AppmasterMindScOperations appmasterScOperations) {
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
