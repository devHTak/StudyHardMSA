package com.example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Event {

    @Id @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_unique_id", unique = true)
    private String eventId;

    private String title;

    private String description;

    private String studyId;

    private String createdBy;

    @Enumerated
    private EventType eventType;

    private LocalDateTime createDateTime;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private boolean useFlag;

    @Min(1)
    private int limitOfEnrollments;

    @JsonManagedReference
    @OneToMany(mappedBy = "event")
    private List<Enrollment> enrollments = new LinkedList<>();

    public void addEnrollment(Enrollment enrollment) {
        if(!enrollments.contains(enrollment)) {
            this.enrollments.add(enrollment);
            enrollment.setEvent(this);
        }
    }

    public void removeEnrollment(Enrollment enrollment) {
        if(enrollments.contains(enrollment)){
            this.enrollments.remove(enrollment);
            enrollment.setEvent(null);
        }
    }

}
