package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Notification {

    private String studyId;

    private ResponseZone zone;

    private ResponseTag tag;

    private String message;
}
