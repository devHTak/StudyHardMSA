package com.example.service;

import com.example.domain.Study;
import com.example.domain.StudyManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyManagerRepository extends JpaRepository<StudyManager, Long> {
    List<StudyManager> findByStudyAndUseFlag(Study study,boolean useFlag);

}
