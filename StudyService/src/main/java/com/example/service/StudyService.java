package com.example.service;

import com.example.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyManagerRepository studyManagerRepository;
    private final StudyMemberRepository studyMemberRepository;


    public Study saveStudy(StudyEntity studyEntity, String managerId) {
        Study study = new Study();
        study.setImage(studyEntity.getImage());
        study.setPath(studyEntity.getPath());
        study.setFullDescription(studyEntity.getFullDescription());
        study.setShortDiscription(studyEntity.getShortDiscription());
        study.setUseBanner(studyEntity.isUseBanner());
        study.setStudyId(UUID.randomUUID().toString());
        study.setCreatedBy(managerId);

        return studyRepository.save(study);

    }

    public Study findByStudyId(String studyId, String managerId) {
        return studyRepository.findByStudyIdAndCreatedBy(studyId, managerId).orElseThrow(()-> new IllegalArgumentException());
    }

    public List<AccountEntity> findManagersByStudyId(String studyId) {
        Study study = studyRepository.findByStudyId(studyId).orElseThrow(()-> new IllegalArgumentException());

        return studyManagerRepository.findByStudyAndUseFlag(study, true).stream()
                .map(studyManager -> {
                    //TODO Account 에서 조회하도록 수정 -> RestTemplate
                    AccountEntity accountEntity = new AccountEntity();
                    accountEntity.setMemberId(studyManager.getManagerId());
                    return accountEntity;
                }).collect(toList());
    }

    public List<AccountEntity> findMembersByStudyId(String studyId) {
        Study study = studyRepository.findByStudyId(studyId).orElseThrow(()-> new IllegalArgumentException());

        return studyMemberRepository.findByStudyAndUseFlag(study, true).stream()
                .map(studyMember -> {
                    //TODO Account 에서 조회하도록 수정 -> RestTemplate
                    AccountEntity accountEntity = new AccountEntity();
                    accountEntity.setMemberId(studyMember.getMemberId());
                    return accountEntity;
                }).collect(toList());
    }

    public boolean joinStudy(String studyId, String memberId) {
        Study study = studyRepository.findByStudyId(studyId)
                .orElseThrow(()->new IllegalArgumentException());

        StudyMember studyMember = studyMemberRepository.findByStudyAndMemberId(study, memberId)
                .orElse(new StudyMember(study, memberId, true));
        studyMember.setUseFlag(true);

        study.addMember(studyMember);
        studyMemberRepository.save(studyMember);
        return true;
    }

    public boolean leaveStudy(String studyId, String memberId) {
        Study study = studyRepository.findByStudyId(studyId)
                .orElseThrow(()->new IllegalArgumentException());

        StudyMember studyMember = studyMemberRepository.findByStudyAndMemberId(study, memberId)
                .orElseThrow(()->new IllegalArgumentException());

        studyMember.setUseFlag(false);
        study.removeMember(studyMember);
        studyMemberRepository.save(studyMember);
        return true;
    }
}
