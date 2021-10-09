package com.example.entity.notification;

import com.example.entity.zone_tag.ResponseTag;
import com.example.entity.zone_tag.ResponseZone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RequestNotification {

    private String studyId;

    private ResponseZone zone;

    private ResponseTag tag;

    private String message;
}
