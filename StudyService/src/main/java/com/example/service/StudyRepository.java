package com.example.service;

import com.example.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findByStudyIdAndCreatedBy(String studyId, String managerId);

    Optional<Study> findByStudyId(String studyId);

    @Query("select s from Study s join fetch s.zones where s.studyId = :studyId")
    Study findByStudyIdWithZone(String studyId);

    @Query("select s from Study s join fetch s.tags where s.studyId = :studyId")
    Study findByStudyIdWithTag(String studyId);
}
