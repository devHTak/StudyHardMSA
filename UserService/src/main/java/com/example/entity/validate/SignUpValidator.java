package com.example.entity.validate;

import com.example.entity.account.SignUpEntity;
import com.example.service.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        // Invalid Target 오류 해결을 위해 확인
        return aClass.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        // Invalid Target 오류 해결을 위해 클래스 확인 필요
        if(!(o instanceof SignUpEntity)) {
            return;
        }
        SignUpEntity entity = (SignUpEntity) o;

        // email, nickname 중복 체크
        boolean existEmail = accountRepository.existsByEmail(entity.getEmail());
        if(existEmail) {
            errors.rejectValue("email", "duplicate.email");
        }

        boolean existNickname = accountRepository.existsByNickname(entity.getNickname());
        if(existNickname) {
            errors.reject("nickname", "duplicate.nickname");
        }
    }
}
