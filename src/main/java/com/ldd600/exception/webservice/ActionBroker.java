package com.ldd600.exception.webservice;

import com.ldd600.exception.dto.Request;
import com.ldd600.exception.dto.Response;

public interface ActionBroker {

	void execute(String actionName, Request request, Response response);

}
