package com.ldd600.exception.util;

public class ClassLoaderUtils {
    public static ClassLoader getClassLoader() {
        ClassLoader clsLoader = Thread.currentThread().getContextClassLoader();
        if(null == clsLoader) {
            clsLoader = ClassLoader.getSystemClassLoader();
        }
        return clsLoader;
    }
}
