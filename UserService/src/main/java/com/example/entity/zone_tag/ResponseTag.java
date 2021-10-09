package com.example.entity.zone_tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ResponseTag {

    @Column(name = "TAG_ID")
    private Long id;

    private String name;
}
