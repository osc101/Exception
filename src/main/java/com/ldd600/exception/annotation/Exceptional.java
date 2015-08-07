package com.ldd600.exception.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ldd600.exception.base.handler.ExceptionHandler;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Exceptional {
    String errorCode();
    Class<? extends ExceptionHandler>[] handlers();
}
