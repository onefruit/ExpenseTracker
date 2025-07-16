package com.prabinsoft.expense.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseStatus {
    FAIL(0), SUCCESS(1);

    private final int code;

    ResponseStatus(int code) {
        this.code = code;
    }

    @JsonValue // Jackson uses this value when serializing
    public int getCode() {
        return code;
    }
}

