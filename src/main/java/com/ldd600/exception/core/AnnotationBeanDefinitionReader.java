package com.ldd600.exception.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.ldd600.exception.util.ClassLoaderUtils;
import com.ldd600.exception.util.ConfigUtils;

public class AnnotationBeanDefinitionReader {
    private Map<String, BeanReader> annotations = new HashMap<String, BeanReader>();
    
    public AnnotationBeanDefinitionReader() {
    }
    
    public void loadBeansWithAnnotation(final BeanDefinitionRegistry registry) {
       load(registry, new AnnotationClassScanner());
    }

    public void loadBeansWithAnnotation(BeanDefinitionRegistry registry, String resourceName, boolean scanDirs, List<String> includePackages, List<String> excludePackages) {
       load(registry, new AnnotationClassScanner(resourceName, scanDirs, includePackages, excludePackages));
    }

    
    @SuppressWarnings("unchecked")
    private void load(final BeanDefinitionRegistry registry, final AnnotationClassScanner scanner) {
       try{
           for (final String resName : scanner.getClassResourceNames()) {
               for (final String typeName : annotations.keySet()) {
                       AnnotationClassVisitor cv = new AnnotationClassVisitor();
                       ClassReader cr = new ClassReader(ClassLoaderUtils.getClassLoader().getResourceAsStream(resName));
                       cr.accept(cv, 0);
                       List<String> clsAnnoNames = cv.getVisibleAnnotationNames();
                       if(clsAnnoNames.contains(typeName)) {
                           String className = ConfigUtils.filenameToClassname(resName);
                           Class c = ClassLoaderUtils.getClassLoader().loadClass(className);
                           BeanReader reader = annotations.get(typeName);
                           if(null == reader) {
                               reader = BeanReaderFactory.getDefaultAnnotationBeanReader();
                          }
                           reader.register(registry, c);
                           break;
                       }
               }
           } 
       }catch(ClassNotFoundException cnfex) {
           
       }catch(IOException ioex) {
           
       }
       
    }


    public void register(String typeName, BeanReader reader) {
        annotations.put(typeName, reader);
    }

}
