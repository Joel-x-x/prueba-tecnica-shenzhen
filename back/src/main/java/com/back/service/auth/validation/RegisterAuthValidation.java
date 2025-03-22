package com.back.service.auth.validation;

import com.back.domain.auth.dto.RegisterRequest;

public interface RegisterAuthValidation {
    public void validate(RegisterRequest request);
}
