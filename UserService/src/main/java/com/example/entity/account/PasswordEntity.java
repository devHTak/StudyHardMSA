package com.example.entity.account;

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
public class PasswordEntity {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String nickname;

    @NotNull
    @Size(min = 8)
    private String password;
}
