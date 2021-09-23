package com.EventService.service;

import com.EventService.entity.Event;
import com.EventService.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event-service/studies/{studyId}/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Event> saveEvent(@Validated @RequestBody EventEntity eventEntity, BindingResult result, @PathVariable String studyId) {
        Event event = this.getEventByEntity(eventEntity);
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(event);
        }

        Event save = eventService.save(event, studyId);
        return ResponseEntity.ok(save);
    }

    @GetMapping
    public ResponseEntity<List<Event>> findAllEvent(@PathVariable String studyId) {
        return ResponseEntity.ok(eventService.findAllEventByStudy(studyId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> findEventById(@PathVariable String studyId, @PathVariable String eventId) {
        Event returnValue = eventService.findEventByStudyAndEvent(studyId, eventId);

        return ResponseEntity.ok(returnValue);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@Validated @RequestBody EventEntity eventEntity, BindingResult result, @PathVariable String studyId, @PathVariable String eventId) {
        Event event = this.getEventByEntity(eventEntity);
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(event);
        }

        Event save = eventService.update(eventEntity, studyId, eventId);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Event> deleteEvent(@PathVariable String studyId, @PathVariable String eventId) {
        return ResponseEntity.ok(eventService.delete(studyId, eventId));
    }


    private Event getEventByEntity(EventEntity eventEntity) {
        Event event = new Event();
        event.setEventType(eventEntity.getEventType());
        event.setDescription(eventEntity.getDescription());
        event.setCreatedBy(eventEntity.getCreatedBy());
        event.setEndDateTime(eventEntity.getEndDateTime());
        event.setLimitOfEnrollments(eventEntity.getLimitOfEnrollments());
        event.setStartDateTime(eventEntity.getStartDateTime());
        event.setStudyId(eventEntity.getStudyId());
        event.setDescription(eventEntity.getDescription());
        event.setTitle(eventEntity.getTitle());

        return event;
    }
}
