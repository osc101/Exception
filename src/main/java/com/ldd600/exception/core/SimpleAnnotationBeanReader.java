package com.ldd600.exception.core;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class SimpleAnnotationBeanReader implements BeanReader {
    private boolean lazy = false;
    private int autoWireMode = Autowire.BY_NAME.value();
    private String scope = "singleton";
    
    /**
     * Default constructor, lazy will be false, autoWire will be by name and scope will be singleton
     */
    public SimpleAnnotationBeanReader()
    {
        super();
    }

    /**
     * Creates a new generic annotation bean reader with specified properties
     * @param lazy defines the lazy mode for the scanned classes
     * @param autoWireMode defines the autoWire mode for the scanned classes, valid values are the constants of the interface
     * @param scope defines the scope of the scanned classes
     */
    public SimpleAnnotationBeanReader(boolean lazy, int autoWireMode, String scope)
    {
        super();
        this.lazy = lazy;
        this.autoWireMode = autoWireMode;
        this.scope = scope;
    }

    /**
     * Creates a new generic annotation bean reader with specified properties
     * @param lazy defines the lazy mode for the scanned classes
     * @param autoWireMode defines the autoWire mode for the scanned classes, valid values are the values of the enumeration 
     * @param scope defines the scope of the scanned classes
     */
    public SimpleAnnotationBeanReader(boolean lazy, String autoWireMode, String scope)
    {
        super();
        this.lazy = lazy;
        this.autoWireMode = Autowire.valueOf(autoWireMode).value();
        this.scope = scope;
    }

    public void setLazy(boolean lazy)
    {
        this.lazy = lazy;
    }

    /**
     * defines the autoWire mode for the scanned classes
     * @param autoWireMode valid values are the constants of the interface
     */
    public void setAutoWireMode(int autoWireMode)
    {
        this.autoWireMode = autoWireMode;
    }

    /**
     * defines the autoWire mode for the scanned classes
     * @param autoWireMode valid values are the values of the enumeration
     */
    public void setAutoWireMode(String autoWireMode)
    {
        this.autoWireMode = Autowire.valueOf(autoWireMode).value();;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    /**
     * method called every time a bean with the configured annotation is read.
     * the bean name will bne the class name with the first letter in lower case, the other properties are read from this instance.
     */
    public void register(BeanDefinitionRegistry registry, Class<?> clazz)
    {
        final RootBeanDefinition rbd = new RootBeanDefinition();
        rbd.setAbstract(false);
        rbd.setBeanClass(clazz);
        rbd.setBeanClassName(clazz.getName());
        rbd.setLazyInit(lazy);
        rbd.setAutowireCandidate(autoWireMode!=AutowireCapableBeanFactory.AUTOWIRE_NO);
        rbd.setAutowireMode(autoWireMode);
        rbd.setScope(scope);
        String name = clazz.getSimpleName();
        name = name.substring(0,1).toLowerCase() + name.substring(1, name.length());
        registry.registerBeanDefinition(name, rbd);
    }
   

}
