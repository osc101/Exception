package com.ldd600.exception.core;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public interface BeanReader {
    void register(BeanDefinitionRegistry registry, Class<?> clazz);
}
