package com.ldd600.exception.base;

import com.ldd600.exception.annotation.Exceptional;
import com.ldd600.exception.base.handler.ConsoleHandler;

@Exceptional(errorCode="LDD600-00002", handlers={ConsoleHandler.class})
public class ConfigException extends BaseAppRuntimeException {

    public ConfigException() {
        super();
    }
    
    public ConfigException(String message) {
       super(message);
    }

    public ConfigException(String message, Throwable cause) {
       super(message, cause);
    }
}
