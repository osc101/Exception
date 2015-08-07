package com.ldd600.exception.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ldd600.exception.base.handler.ExceptionHandler;
import com.ldd600.exception.dto.Response;

public class DefaultExceptionHandler implements ExceptionHandler {
	private static Log logger = LogFactory.getLog(DefaultExceptionHandler.class);

	public Object handleException(String errorCode, Exception bex,
			Response response) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
