package com.EventService.service;

import com.EventService.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByEventIdAndAccountId(Long eventId, String accountId);

    int countByEventId(Long id);

    List<Enrollment> findByEventId(Long eventId);
}
