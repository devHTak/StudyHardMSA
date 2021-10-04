package com.example.entity;

import com.example.service.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ZoneValidator implements Validator {

    private final ZoneRepository zoneRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Address.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Address address = (Address) target;

        if(zoneRepository.existsByAddressCityAndAddressLocalNameCityAndAddressProvince(address.getCity(), address.getLocalNameCity(), address.getProvince())) {
            errors.reject("address", "duplicated.address");
        }
    }
}
