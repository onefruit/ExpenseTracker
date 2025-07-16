package com.prabinsoft.expense.exception;

import com.prabinsoft.expense.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalApiResponse {
    private ResponseStatus responseStatus;
    private String message;
    private Object data;
}
