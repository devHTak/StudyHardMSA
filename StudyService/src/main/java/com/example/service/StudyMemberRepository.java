package com.example.service;

import com.example.domain.Study;
import com.example.domain.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    List<StudyMember> findByStudyAndUseFlag(Study study, boolean useFlag);

    Optional<StudyMember> findByStudyAndMemberId(Study study, String memberId);

}
