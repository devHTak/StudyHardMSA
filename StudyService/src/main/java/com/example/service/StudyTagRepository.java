package com.example.service;

import com.example.domain.StudyTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyTagRepository extends JpaRepository<StudyTag, Long> {
}
