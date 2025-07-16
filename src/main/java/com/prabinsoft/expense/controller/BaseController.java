package com.prabinsoft.expense.controller;

import com.prabinsoft.expense.enums.ResponseStatus;
import com.prabinsoft.expense.exception.GlobalApiResponse;

public class BaseController {

    private static final ResponseStatus API_SUCCESS_STATUS = ResponseStatus.SUCCESS;
    private static final ResponseStatus API_FAIL_STATUS = ResponseStatus.FAIL;


    protected GlobalApiResponse successResponse(String message, Object data) {
        GlobalApiResponse globalApiResponse = new GlobalApiResponse();
        globalApiResponse.setResponseStatus(API_SUCCESS_STATUS);
        globalApiResponse.setMessage(message);
        globalApiResponse.setData(data);
        return globalApiResponse;
    }

    protected GlobalApiResponse failResponse(String message, Object data) {
        GlobalApiResponse globalApiResponse = new GlobalApiResponse();
        globalApiResponse.setResponseStatus(API_FAIL_STATUS);
        globalApiResponse.setMessage(message);
        globalApiResponse.setData(data);
        return globalApiResponse;
    }
}
