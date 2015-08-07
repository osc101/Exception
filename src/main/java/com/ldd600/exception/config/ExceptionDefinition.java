package com.ldd600.exception.config;

import java.util.LinkedHashSet;
import java.util.Set;

public class ExceptionDefinition {
    private String errorCode;

    private Set<String> handlerNames = new LinkedHashSet<String> ();

    ExceptionDefinition() {
        
    }
    
    public ExceptionDefinition(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Set<String> getHandlerNames() {
        return handlerNames;
    }
}
