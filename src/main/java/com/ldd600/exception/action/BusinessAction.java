package com.ldd600.exception.action;

import com.ldd600.exception.base.BaseAppException;
import com.ldd600.exception.dto.Request;
import com.ldd600.exception.dto.Response;

public interface BusinessAction<OUT extends Response, IN extends Request> {

	/**
	 * 
	 * @param request
	 * @param response TODO
	 */
	void execute(IN request, OUT response) throws BaseAppException;
}
