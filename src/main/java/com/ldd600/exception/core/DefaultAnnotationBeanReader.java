package com.ldd600.exception.core;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.ldd600.exception.util.ConfigUtils;

public class DefaultAnnotationBeanReader implements BeanReader{

    public void register(BeanDefinitionRegistry registry, Class<?> clazz) {
        final BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        registry.registerBeanDefinition(ConfigUtils.getBeanNameFromClass(clazz), builder.getBeanDefinition());
    }

}
