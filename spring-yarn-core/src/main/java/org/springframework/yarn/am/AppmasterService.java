package org.springframework.yarn.am;

/**
 * Interface for service running on Application Master.
 * <p>
 * Usually this service provides a simple communication
 * api for client containers to connect to.
 * 
 * @author Janne Valkealahti
 *
 */
public interface AppmasterService {

    /**
     * Get a port where service is running. This method
     * should return port as zero or negative if port is
     * unknown. For example if underlying communication
     * library is using random ports or other methods so
     * that user doesn't need to worry about it.
     * 
     * @return Service port, -1 if port unknown.
     */
    int getPort();

    /**
     * This method should return true if a service
     * will eventually bind to a port. User can then 
     * do a sleep while waiting {@link #getPort()} to
     * return the actual port number.
     * 
     * @return True if this service will provide a port
     */
    boolean hasPort();
    
    /**
     * Get a hostname where service is running. This method
     * should return null if service is unknown. 
     * 
     * @return Hostname of the server or null if unknown.
     */
    String getHost();
    
    
}
