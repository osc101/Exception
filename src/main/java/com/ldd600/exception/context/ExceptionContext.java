package com.ldd600.exception.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

import com.ldd600.exception.base.BaseAppRuntimeException;
import com.ldd600.exception.base.ConfigException;
import com.ldd600.exception.base.handler.ExceptionHandler;
import com.ldd600.exception.config.ExceptionDefinition;

public class ExceptionContext {
    private Map<Class<?>, ExceptionDefinition> exceptionMap;

    private Map<String, ExceptionHandler> handlers = new HashMap<String, ExceptionHandler>();

    ExceptionContext() {
        exceptionMap = new HashMap<Class<?>, ExceptionDefinition>();
    }

    public boolean containsException(Class<?> expClazz) {
        return (exceptionMap.containsKey(expClazz));
    }
    
    public void addExceptionHander(Class<?> expClazz, Class<? extends ExceptionHandler> handlerClazz) {
        try {
            ExceptionDefinition definition = getRealExceptionDefinition(expClazz);
            if (null == definition) {
                throw new IllegalArgumentException(expClazz.getName() + "not in the context, please configure or add it to the context first!!");
            } 
            ExceptionHandler handler = handlers.get(handlerClazz.getName());
            if (null == handler) {
                handler = handlerClazz.newInstance();
                handlers.put(handlerClazz.getName(), handler);
            }
            
            definition.getHandlerNames().add(handlerClazz.getName());
        } catch (Exception ex) {
            throw new ConfigException("Add exception handler to context failure!", ex);
        }
    }
    
    public void addExceptionHandler(Class<?> expClazz, String errorCode, Class<? extends ExceptionHandler> handlerClazz) {
        Assert.hasLength(errorCode, expClazz + " errorCode must not be null or empty string!");
        ExceptionDefinition definition = getRealExceptionDefinition(expClazz);
        if(null == definition) {
            definition = new ExceptionDefinition(errorCode);
            exceptionMap.put(expClazz, definition);
        }
        addExceptionHander(expClazz, handlerClazz);
    }
    
    
    
    public void addExceptionHandlers(Class<?> expClazz, Class<? extends ExceptionHandler>... handlerClazzes) {
        for(Class<? extends ExceptionHandler> handlerClazz : handlerClazzes) {
            addExceptionHander(expClazz, handlerClazz);
        }
    }

    public void removeExceptionHandler(Class<?> expClazz, Class<? extends ExceptionHandler> handlerClazz) {
        Assert.isTrue(containsException(expClazz));
        String handlerName = handlerClazz.getName();
        getExceptionDefinition(expClazz).getHandlerNames().remove(handlerName);
        Collection<ExceptionDefinition> definitons = exceptionMap.values();
        boolean isClearHandler = true;
        for (ExceptionDefinition expDefinition : definitons) {
            if (expDefinition.getHandlerNames().contains(handlerName)) {
                isClearHandler = false;
                break;
            }
        }

        if (isClearHandler) {
            handlers.remove(handlers.get(handlerName));
        }
    }

    public void setExceptionDefinition(Class<?> expClazz, ExceptionDefinition definition) {
        exceptionMap.put(expClazz, definition);
    }

    public ExceptionDefinition getExceptionDefinition(Class<?> expClazz) {
        if (containsException(expClazz)) {
            return exceptionMap.get(expClazz);  
        } else if (BaseAppRuntimeException.class.isAssignableFrom(expClazz.getSuperclass())) {
            return getExceptionDefinition(expClazz.getSuperclass());
        } else {
            return null;
        }
    }
    
    public ExceptionDefinition getRealExceptionDefinition(Class<?> expClazz) {
        return exceptionMap.get(expClazz);
    }

    public List<ExceptionHandler> getExceptionHandlers(Class<?> expClazz){
        ExceptionDefinition definition = getExceptionDefinition(expClazz);
        if (null != definition) {
            Set<String> handlerNames = definition.getHandlerNames();
            List<ExceptionHandler> handlerList = new ArrayList<ExceptionHandler>(handlerNames.size());
            for (String handlerName : handlerNames) {
                ExceptionHandler handler = handlers.get(handlerName);
                handlerList.add(handler);
            }
            List<ExceptionHandler> resultHandlerList = new ArrayList<ExceptionHandler>(handlerList);
            return resultHandlerList;
        } else {
            return Collections.<ExceptionHandler> emptyList();
        }
    }
    
    public String getErrorCode(Class<?> expClazz){
        ExceptionDefinition definition = getExceptionDefinition(expClazz);
        if (null != definition) {
            return definition.getErrorCode();
        } else {
            return "";
        }
    }
    
    
}
