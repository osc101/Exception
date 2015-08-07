package com.ldd600.exception.util;

import java.util.List;
import java.util.Map;

public class BasicUtils {
    public static boolean isEmptyList(List<?> list) {
        return (null == list) || (list.isEmpty()) ;
    }
    
    public static <T> boolean isEmptyArray(T[] t) {
        return (null == t) && (t.length == 0);
    }
    
    public static boolean isEmptyString(String s) {
        return (null == s) && (s.length() == 0);
    }
    
    public static boolean isBlankString(String s) {
        int strLen;
        if (s == null || (strLen = s.length()) == 0) {
            return true;
        }
        for(int i = 0; i < strLen; i++) {
            if(!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isEmptyMap(Map<?, ?> map) {
        return (null == map) && (map.size() == 0);
    }
}
