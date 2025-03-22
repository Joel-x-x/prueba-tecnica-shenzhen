package com.back.service.auth.validation;

import com.back.domain.auth.dto.RegisterRequest;
import com.back.domain.user.entity.UserEntity;
import com.back.infrastructure.exception.IntegrityValidation;
import com.back.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniqueEmailValidation implements RegisterAuthValidation{

    private final UserRepository userRepository;

    @Override
    public void validate(RegisterRequest request) {
        if(this.userRepository.existsUserEntitiesByEmail(request.email())) {
            throw new IntegrityValidation("Email already exists");
        }
    }
}
