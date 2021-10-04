package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Address {
    @NotNull
    private String city;

    @NotNull
    private String localNameCity;

    @NotNull
    private String province;
}
