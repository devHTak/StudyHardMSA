package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EventEntity {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String studyId;

    private EventType eventType;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Min(1)
    private int limitOfEnrollments;
}
