package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Zone {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

}
