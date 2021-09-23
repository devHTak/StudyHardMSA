package com.EventService.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Enrollment {

    @Id @GeneratedValue
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private String accountId;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;

    private boolean useFlag;

    public void setAccepted(Event event, int size) {
        if(event.getEventType().equals(EventType.CONFIRMATIVE)) {
            this.accepted = false;
        } else {
            if(canEnrollment(event, size)) {
                this.accepted = true;
            } else {
                this.accepted = false;
            }
        }
    }

    private boolean canEnrollment(Event event, int size) {
        return event.getLimitOfEnrollments() > size + 1;
    }
}
