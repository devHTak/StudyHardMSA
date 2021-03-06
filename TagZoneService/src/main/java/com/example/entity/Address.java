package com.example.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = {"city", "localNameCity", "province"})
public class Address {
    @NotNull
    private String city;
    @NotNull
    private String localNameCity;
    @NotNull
    private String province;
}
