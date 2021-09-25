package com.example.service;

import com.example.entity.Event;
import com.example.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event-service/members/{memberId}/studies/{studyId}/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Event> saveEvent(@Validated @RequestBody EventEntity eventEntity, BindingResult result, @PathVariable String studyId, @PathVariable String memberId) {
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(null);
        }
        Event save = eventService.save(eventEntity, studyId, memberId);
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
    public ResponseEntity<Event> updateEvent(@Validated @RequestBody EventEntity eventEntity, BindingResult result, @PathVariable String studyId, @PathVariable String eventId, @PathVariable String memberId) {
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(null);
        }

        Event save = eventService.update(eventEntity, studyId, eventId, memberId);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Event> deleteEvent(@PathVariable String studyId, @PathVariable String eventId) {
        return ResponseEntity.ok(eventService.delete(studyId, eventId));
    }
}
