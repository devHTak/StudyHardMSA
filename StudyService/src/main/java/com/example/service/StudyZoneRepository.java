package com.example.service;

import com.example.domain.StudyZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyZoneRepository extends JpaRepository<StudyZone, Long> {
    Optional<StudyZone> findByZoneId(Long zoneId);
}
