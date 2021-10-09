package com.example.entity.zone_tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Address {
    private String city;

    private String localNameCity;

    private String province;
}
