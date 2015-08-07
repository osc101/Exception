package com.ldd600.exception.base.handler;

import com.ldd600.exception.dto.Response;

public interface ExceptionHandler {
	Object handleException(String errorCode, Exception bex, Response response);
}
