package com.example.service;

import com.example.domain.StudyTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyTagRepository extends JpaRepository<StudyTag, Long> {
    Optional<StudyTag> findByTagId(Long tagId);
}
