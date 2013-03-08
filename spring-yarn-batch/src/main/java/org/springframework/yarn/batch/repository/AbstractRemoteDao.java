package org.springframework.yarn.batch.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.yarn.client.AppmasterScOperations;

public abstract class AbstractRemoteDao {

    private AppmasterScOperations appmasterScOperations;
    
    public AbstractRemoteDao() {
    }

    public AbstractRemoteDao(AppmasterScOperations appmasterScOperations) {
        this.appmasterScOperations = appmasterScOperations;
    }
    
    public AppmasterScOperations getAppmasterScOperations() {
        return appmasterScOperations;
    }

    public void setAppmasterScOperations(AppmasterScOperations appmasterScOperations) {
        this.appmasterScOperations = appmasterScOperations;
    }
    
    protected DataAccessException convertException(Exception e) {
        return new DataRetrievalFailureException(e.getMessage(), e);
    }

}
