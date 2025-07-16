package com.prabinsoft.expense.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {
    SAVE("success.save");
    private final String code;
}
