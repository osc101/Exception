package com.ldd600.exception.base.handler;

import com.ldd600.exception.dto.Response;

public class ConsoleHandler implements ExceptionHandler{

    public Object handleException(String errorCode, Exception bex, Response response) {
        System.out.println("Console Exception Hander catch exception: " + errorCode + " -- " + bex.getClass());
        response.setSuccess(false);
        response.setErrorCode(errorCode);
        response.setMessage(bex.getMessage());
        return null;
    }

}
