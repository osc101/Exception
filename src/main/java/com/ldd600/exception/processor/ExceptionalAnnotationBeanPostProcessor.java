package com.ldd600.exception.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.ldd600.exception.annotation.Exceptional;
import com.ldd600.exception.base.BaseAppException;
import com.ldd600.exception.base.BaseAppRuntimeException;
import com.ldd600.exception.config.ExceptionDefinition;
import com.ldd600.exception.context.ExceptionContext;
import com.ldd600.exception.context.CoreContextFactory;

public class ExceptionalAnnotationBeanPostProcessor implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
       if(bean instanceof BaseAppRuntimeException || bean instanceof BaseAppException) {
           Exceptional exceptional = bean.getClass().getAnnotation(Exceptional.class);
           if(null != exceptional) {
               ExceptionContext ctx = CoreContextFactory.getInstance().getExceptionContext();
               if(!ctx.containsException(bean.getClass())) {
                   ExceptionDefinition expDefinition = new ExceptionDefinition(exceptional.errorCode());
                   ctx.setExceptionDefinition(bean.getClass(), expDefinition);
               }
               ctx.addExceptionHandlers(bean.getClass(), exceptional.handlers());
               return null;
           }
       }
       return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
    }

}
