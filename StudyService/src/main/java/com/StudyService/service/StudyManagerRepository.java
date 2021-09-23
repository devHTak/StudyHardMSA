package com.StudyService.service;

import com.StudyService.domain.Study;
import com.StudyService.domain.StudyManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyManagerRepository extends JpaRepository<StudyManager, Long> {
    List<StudyManager> findByStudyAndUseFlag(Study study,boolean useFlag);

}
