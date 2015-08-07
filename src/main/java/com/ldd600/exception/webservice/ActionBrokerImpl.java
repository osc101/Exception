package com.ldd600.exception.webservice;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.ldd600.exception.action.BusinessAction;
import com.ldd600.exception.base.BaseAppException;
import com.ldd600.exception.base.BaseAppRuntimeException;
import com.ldd600.exception.base.handler.ExceptionHandler;
import com.ldd600.exception.context.CoreContextFactory;
import com.ldd600.exception.context.ExceptionContext;
import com.ldd600.exception.dto.Request;
import com.ldd600.exception.dto.Response;
import com.ldd600.exception.util.BasicUtils;

public class ActionBrokerImpl implements ActionBroker, BeanFactoryAware {
    protected transient Log logger = LogFactory.getLog(getClass());

    private ExceptionHandler exceptionHandler;

    private BeanFactory beanFactory;

    public ActionBrokerImpl() {

    }
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public ActionBrokerImpl(BeanFactory beanFactory, ExceptionHandler exceptionHandler) {
        this.beanFactory = beanFactory;
        this.exceptionHandler = exceptionHandler;
    }

    @SuppressWarnings("unchecked")
    public final void execute(String actionName, Request request, Response response) {
        BusinessAction action = null;
        try {
            action = (BusinessAction) beanFactory.getBean(actionName);
            action.execute(request, response);
        }catch(BaseAppException e){
        	handleException(e, response);
        }catch (BaseAppRuntimeException e) {
        	handleException(e, response);
        } finally {
        	//clear context if necessary
        }

    }
    
    private final void handleException(Exception e, Response response) {
        ExceptionContext context = CoreContextFactory.getInstance().getExceptionContext();
        List<ExceptionHandler> handlers = context.getExceptionHandlers(e.getClass());
        if (BasicUtils.isEmptyList(handlers)) {
        	if(e instanceof BaseAppRuntimeException) {
        		 BaseAppRuntimeException bex = (BaseAppRuntimeException) e;
        		 this.exceptionHandler.handleException(bex.getErrorCode() == null ? "" : bex.getErrorCode(), e,
                           response);
        	}else if(e instanceof BaseAppException) {
        		 BaseAppException bex = (BaseAppException) e;
        		 this.exceptionHandler.handleException(bex.getErrorCode() == null ? "" : bex.getErrorCode(), e,
                           response);
        	}else{
        		 this.exceptionHandler.handleException("", e, response);
        	}
           
         
        } else {
            String errorCode = context.getErrorCode(e.getClass());
            for (ExceptionHandler handler : handlers) {
                handler.handleException(errorCode, e, response);
            }
        }
    }
    
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
