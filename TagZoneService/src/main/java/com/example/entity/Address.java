package com.example.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = {"city", "localNameCity", "province"})
public class Address {

    private String city;

    private String localNameCity;

    private String province;
}
