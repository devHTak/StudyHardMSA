package com.example.service;

import com.example.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findByStudyIdAndCreatedBy(String studyId, String managerId);

    Optional<Study> findByStudyId(String studyId);
}
