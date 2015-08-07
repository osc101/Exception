package com.ldd600.exception.util;

public class ConfigUtils {
    public static String packageNameToRegex(String packageName) {
        return packageName.replace(".", "/") + ".*";
    }
    
    /**
     * method used to convert a file name to a class name, it replaces all '/' or '\' with a '.' and removes the
     * '.class' ending
     * 
     * @param filename file name to convert
     * @return converted class name
     */
    public static String filenameToClassname(final String filename) {
        return filename.substring(0, filename.lastIndexOf(".class")).replace('/', '.').replace('\\', '.');
    }
    
    
    public static String convertClassNameToBeanName(String simpleClassName) {
        char begin = simpleClassName.charAt(0);
        char lowBegin = Character.toLowerCase(begin);
        char[] actionNameChars = new char[simpleClassName.length()];
        simpleClassName.getChars(1, simpleClassName.length(), actionNameChars, 1);
        actionNameChars[0] = lowBegin;
        return new String(actionNameChars);
    }
    
    public static String getBeanNameFromClass(Class<?> clazz) {
        String simpleClassName = clazz.getSimpleName();
        return convertClassNameToBeanName(simpleClassName);
    }
    
  
}
