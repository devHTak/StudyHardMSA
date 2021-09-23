package com.StudyService.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
public class Study {

    @Id @GeneratedValue
    @Column(name = "study_id")
    private Long id;

    private String title;

    @Column(name="study_unique_id", unique = true)
    private String studyId;

    private String shortDiscription;
    private String path;
    private String image;

    @Lob
    private String fullDescription;

    private boolean useBanner;
    private LocalDateTime createdTime;

    private boolean published;
    private LocalDateTime publishedTime;

    private boolean recruiting;
    private LocalDateTime recruitedUpdateTime;

    private boolean closed;
    private LocalDateTime closedUpdateTime;

    @OneToMany(mappedBy = "study")
    private List<StudyMember> members = new LinkedList<>();

    @OneToMany(mappedBy = "study")
    private List<StudyManager> managers = new LinkedList<>();

    public void addMember(StudyMember member) {
        if(!members.contains(member)) {
            members.add(member);
            member.setStudy(this);
        }
    }

    public void removeMember(StudyMember member) {
        if(members.contains(member)) {
            members.remove(member);
            member.setStudy(null);
        }
    }

    public void addManager(StudyManager manager) {
        if(!managers.contains(manager)) {
            managers.add(manager);
            manager.setStudy(this);
        }
    }

    public void removeManager(StudyManager manager) {
        if(managers.contains(manager)) {
            managers.remove(manager);
            manager.setStudy(null);
        }
    }

}
