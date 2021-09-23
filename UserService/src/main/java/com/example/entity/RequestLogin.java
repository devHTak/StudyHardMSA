package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RequestLogin {

    @NotNull
    @Size(min = 2)
    @Email
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;
}
