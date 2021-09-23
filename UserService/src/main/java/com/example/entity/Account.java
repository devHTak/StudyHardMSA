package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String nickname;

    @NotNull
    @JsonIgnore
    private String password;

    private String userId; // api

    private LocalDateTime createdAt;

    private String bio;
    private String url;
    private String occupation;
    private String location;
}
