package com.ldd600.exception.webservice;

import com.ldd600.exception.dto.Request;
import com.ldd600.exception.dto.Response;

public class AbstractWebService {
    private ActionBroker broker;

    protected <OUT extends Response, IN extends Request> OUT execute(String actionName, IN request, OUT response) {
        broker.execute(actionName, request, response);
        return response;
    }

}
