package com.example.entity.zone_tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ResponseZone {

    @Column(name = "ZONE_ID")
    private Long id;

    @Embedded
    private Address address;
}
