package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SignUpEntity {
    @NotNull(message = "Email is not null")
    @Email(message = "Email format")
    private String email;

    @NotNull(message = "nickname is not null")
    private String nickname;

    @NotNull(message = "password is not null")
    @Size(min = 8, message = "password is greater than 8")
    private String password;
}
