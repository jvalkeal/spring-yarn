package org.springframework.yarn.integration.convert;

import org.springframework.core.convert.ConversionException;

/**
 * Simple exception indicating errors during the conversion.
 * 
 * @author Janne Valkealahti
 * 
 */
@SuppressWarnings("serial")
public class MindDataConversionException extends ConversionException {
    
    public MindDataConversionException(String message, Throwable cause) {
        super(message, cause);
    }
    
}