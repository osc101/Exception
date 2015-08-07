package com.ldd600.exception.core;

public class BeanReaderFactory {
    private static DefaultAnnotationBeanReader defaultAnnBeanReader;
    private static SimpleAnnotationBeanReader simpleAnnBeanReader;
    
    public static synchronized BeanReader getDefaultAnnotationBeanReader() {
        if(null == defaultAnnBeanReader) {
            defaultAnnBeanReader = new DefaultAnnotationBeanReader();
        }
        return defaultAnnBeanReader;
    }
    
    public static synchronized BeanReader getSimpleAnnotationBeanReader() {
        if(null == simpleAnnBeanReader) {
            simpleAnnBeanReader = new SimpleAnnotationBeanReader();
        }
        return simpleAnnBeanReader;
    }
}
