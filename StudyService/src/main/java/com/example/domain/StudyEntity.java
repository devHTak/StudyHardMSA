package com.example.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class StudyEntity {

    @NotNull
    private String title;

    private String shortDiscription;
    private String path;
    private String image;

    @Lob @NotNull
    private String fullDescription;

    private boolean useBanner;
}
