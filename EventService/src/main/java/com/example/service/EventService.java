package com.example.service;

import com.example.entity.Event;
import com.example.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;


    public Event save(EventEntity eventEntity, String studyId, String memberId) {
        Event event = this.getEventByEntity(eventEntity);
        event.setStudyId(studyId);
        event.setCreatedBy(memberId);
        event.setCreateDateTime(LocalDateTime.now());
        event.setEventId(UUID.randomUUID().toString());
        event.setUseFlag(true);

        return eventRepository.save(event);
    }

    public Event findEventByStudyAndEvent(String studyId, String eventId) {
        return eventRepository.findByStudyIdAndEventIdAndUseFlag(studyId, eventId, true)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Event> findAllEventByStudy(String studyId) {
        return eventRepository.findByStudyIdAndUseFlag(studyId, true);
    }

    public Event update(EventEntity eventEntity, String studyId, String eventId, String memberId) {
        Event returnEvent = eventRepository.findByStudyIdAndEventIdAndUseFlag(studyId, eventId, true)
                .orElseThrow(IllegalArgumentException::new);
        returnEvent = updateEventByEntity(returnEvent, eventEntity);
        returnEvent.setCreatedBy(eventId);

        return returnEvent;
    }

    public Event delete(String studyId, String eventId) {
        Event returnEvent = eventRepository.findByStudyIdAndEventIdAndUseFlag(studyId, eventId, true)
                .orElseThrow(IllegalArgumentException::new);
        returnEvent.setUseFlag(false);

        return returnEvent;
    }

    private Event updateEventByEntity(Event event, EventEntity eventEntity) {
        event.setEventType(eventEntity.getEventType());
        event.setDescription(eventEntity.getDescription());
        event.setEndDateTime(eventEntity.getEndDateTime());
        event.setLimitOfEnrollments(eventEntity.getLimitOfEnrollments());
        event.setStartDateTime(eventEntity.getStartDateTime());
        event.setStudyId(eventEntity.getStudyId());
        event.setDescription(eventEntity.getDescription());
        event.setTitle(eventEntity.getTitle());

        return event;
    }

    private Event getEventByEntity(EventEntity eventEntity) {
        Event event = new Event();
        event.setEventType(eventEntity.getEventType());
        event.setDescription(eventEntity.getDescription());
        event.setEndDateTime(eventEntity.getEndDateTime());
        event.setLimitOfEnrollments(eventEntity.getLimitOfEnrollments());
        event.setStartDateTime(eventEntity.getStartDateTime());
        event.setStudyId(eventEntity.getStudyId());
        event.setDescription(eventEntity.getDescription());
        event.setTitle(eventEntity.getTitle());

        return event;
    }
}
