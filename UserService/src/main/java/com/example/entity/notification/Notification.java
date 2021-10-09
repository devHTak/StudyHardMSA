package com.example.entity.notification;

import com.example.entity.account.Account;
import com.example.entity.zone_tag.ResponseTag;
import com.example.entity.zone_tag.ResponseZone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Notification {

    @Id @GeneratedValue
    private Long id;

    private String studyId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Embedded
    private ResponseZone zone;

    @Embedded
    private ResponseTag tag;

    private String message;
}
