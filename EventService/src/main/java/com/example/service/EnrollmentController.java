package com.example.service;

import com.example.entity.Enrollment;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event-service/members/{memberId}/events/{eventId}/enrollments")
@AllArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<Enrollment> enrollEvent(@PathVariable String memberId, @PathVariable String eventId) {
        return ResponseEntity.ok(enrollmentService.enroll(memberId, eventId));
    }

    @PostMapping("/disenroll")
    public ResponseEntity<Enrollment> disenrollEvent(@PathVariable String memberId, @PathVariable String eventId) {
        return ResponseEntity.ok(enrollmentService.disenroll(eventId, memberId));
    }

    @PostMapping("/{enrollmentId}/accept")
    public ResponseEntity<Enrollment> acceptEvent(@PathVariable String eventId, @PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.accept(enrollmentId));
    }

    @PostMapping("/{enrollmentId}/reject")
    public ResponseEntity<Enrollment> rejectEvent(@PathVariable String eventId, @PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.reject(enrollmentId));
    }

    @PostMapping("/{enrollmentId}/check-in")
    public ResponseEntity<Enrollment> checkInEvent(@PathVariable String eventId, @PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.checkIn(enrollmentId));
    }

    @PostMapping("/{enrollmentId}/cancel-check-in")
    public ResponseEntity<Enrollment> cancelCheckInEvent(@PathVariable String eventId, @PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.cancelCheckIn(enrollmentId));
    }
}
