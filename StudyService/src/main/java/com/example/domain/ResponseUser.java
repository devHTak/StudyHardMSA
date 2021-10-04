package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class ResponseUser {

    private String email;

    private String nickname;

    private String userId;
    private LocalDateTime createdAt;

    private String bio;
    private String url;
    private String occupation;
    private String location;
}
