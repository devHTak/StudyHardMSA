package com.example.entity;

import com.example.service.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TagValidator implements Validator {

    private final TagRepository tagRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return TagEntity.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TagEntity entity = (TagEntity) target;

        if(tagRepository.existsByName(entity.getName())) {
            errors.rejectValue("name", "duplicated.name");
        }
    }
}
