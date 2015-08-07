package com.ldd600.exception.test;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class DependencyInjectionExceptionTestCase extends AbstractDependencyInjectionSpringContextTests {
    public void assertThrowing(Closure closure, Class<? extends Throwable> clazz) {
        try{
            closure.run();
        }catch(Throwable t){
            assertEquals(t.getClass(), clazz);
        }
    }
}
