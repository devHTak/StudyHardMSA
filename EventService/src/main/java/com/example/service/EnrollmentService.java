package com.example.service;

import com.example.entity.Enrollment;
import com.example.entity.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final EventRepository eventRepository;

    public Enrollment enroll(String memberId, String eventId) {
        Event event = eventRepository.findByEventIdAndUseFlag(eventId, true)
                .orElseThrow(IllegalArgumentException::new);
        int size = enrollmentRepository.countByEventId(event.getId());

        Enrollment enrollment = new Enrollment();
        enrollment.setUseFlag(true);
        enrollment.setAccepted(event, size);
        enrollment.setAttended(false);
        enrollment.setMemberId(memberId);
        enrollment.setEnrolledAt(LocalDateTime.now());
        event.addEnrollment(enrollment);
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment disenroll(String eventId, String memberId) {
        Event event = eventRepository.findByEventIdAndUseFlag(eventId, true)
                .orElseThrow(IllegalArgumentException::new);

        Enrollment enrollment = enrollmentRepository.findByEventIdAndMemberId(event.getId(), memberId)
                .orElseThrow(IllegalArgumentException::new);

        enrollment.setUseFlag(false);
        event.removeEnrollment(enrollment);
        return enrollment;
    }


    public Enrollment accept(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(IllegalArgumentException::new);
        enrollment.setAccepted(true);
        return enrollment;
    }

    public Enrollment reject(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(IllegalArgumentException::new);
        enrollment.setAccepted(false);
        return enrollment;
    }

    public Enrollment checkIn(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(IllegalArgumentException::new);
        enrollment.setAttended(true);
        return enrollment;
    }

    public Enrollment cancelCheckIn(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(IllegalArgumentException::new);
        enrollment.setAttended(false);
        return enrollment;
    }
}
