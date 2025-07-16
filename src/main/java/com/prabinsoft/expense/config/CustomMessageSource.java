package com.prabinsoft.expense.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMessageSource {

    private final MessageSource messageSource;

    /**
     * Get message by key with no parameters.
     */
    public String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * Get message by key with parameters (e.g., placeholders).
     */
    public String getMessage(String key, Object... params) {
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

    /**
     * Get message by key with default message if not found.
     */
    public String getMessage(String key, Object[] params, String defaultMessage) {
        return messageSource.getMessage(key, params, defaultMessage, LocaleContextHolder.getLocale());
    }
}
