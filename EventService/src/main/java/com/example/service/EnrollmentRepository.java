package com.example.service;

import com.example.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByEventIdAndMemberId(Long eventId, String memberId);

    int countByEventId(Long id);

    List<Enrollment> findByEventId(long eventId);
}
