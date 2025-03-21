package com.back.infrastructure.exception;

import java.util.List;

public record ValidationErrorResponse(
        String field,
        List<String> messages
) {
}
