package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Tag {
    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String name;
}
