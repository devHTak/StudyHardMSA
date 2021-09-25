package com.example.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
public class StudyMember {

    @Id @GeneratedValue
    @Column(name = "study_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @NotNull
    private String memberId;

    private boolean useFlag;

    public StudyMember(){}
    public StudyMember(Study study, String memberId, boolean useFlag) {
        this.study = study; this.memberId = memberId; this.useFlag = useFlag;
    }
}
