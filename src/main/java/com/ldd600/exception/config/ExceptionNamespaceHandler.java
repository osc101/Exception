package com.ldd600.exception.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class ExceptionNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        this.registerBeanDefinitionParser("config", new ExceptionBeanDefinitionParser());
    }
}
