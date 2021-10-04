package com.example.service;

import com.example.client.TagServiceClient;
import com.example.client.UserServiceClient;
import com.example.client.ZoneServiceClient;
import com.example.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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
    private final UserServiceClient userServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final ZoneServiceClient zoneServiceClient;
    private final TagServiceClient tagServiceClient;


    public Study saveStudy(RequestStudy studyEntity, String managerId) {
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

    public List<ResponseUser> findManagersByStudyId(String studyId, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyId(studyId).orElseThrow(()-> new IllegalArgumentException());

        return studyManagerRepository.findByStudyAndUseFlag(study, true).stream()
                .map(studyManager -> {
                    ResponseUser user = circuitBreaker.run(() -> userServiceClient.getUser(studyManager.getManagerId(), auth));
                    return user;
                }).collect(toList());
    }

    public List<ResponseUser> findMembersByStudyId(String studyId, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyId(studyId).orElseThrow(()-> new IllegalArgumentException());

        return studyMemberRepository.findByStudyAndUseFlag(study, true).stream()
                .map(studyMember -> {
                    ResponseUser user = circuitBreaker.run(() -> userServiceClient.getUser(studyMember.getMemberId(), auth));
                    return user;
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

    public Study addZone(String studyId, RequestZone requestZone, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyIdWithZone(studyId);
        StudyZone zone = new StudyZone();
        if(!circuitBreaker.run(()-> zoneServiceClient.existZone(requestZone.getAddress().getCity()
                ,requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth))) {
            zone.setZoneId(circuitBreaker.run(() -> zoneServiceClient.addZone(requestZone).getId()));
        } else {
            zone.setZoneId(circuitBreaker.run(() -> zoneServiceClient.findZone(requestZone.getAddress().getCity()
                    ,requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth).getId()));
        }

        study.addZone(zone);
        return study;
    }

    public Study removeZone(String studyId, RequestZone requestZone, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyIdWithZone(studyId);
        StudyZone zone = new StudyZone();
        zone.setZoneId(circuitBreaker.run(() -> zoneServiceClient.findZone(requestZone.getAddress().getCity()
                ,requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth).getId()));

        study.removeZone(zone);
        return study;
    }

    public Study addTag(String studyId, RequestTag requestTag, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyIdWithTag(studyId);
        StudyTag tag = new StudyTag();
        if(!circuitBreaker.run(()-> tagServiceClient.existTag(requestTag.getName(), auth))) {
            tag.setTagId(circuitBreaker.run(() -> tagServiceClient.saveTag(requestTag, auth).getId()));
        } else {
            tag.setTagId(circuitBreaker.run(() -> tagServiceClient.findTag(requestTag.getName(), auth).getId()));
        }

        study.addTag(tag);
        return study;
    }

    public Study removeTag(String studyId, RequestTag requestTag, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyIdWithTag(studyId);
        StudyTag tag = new StudyTag();
        tag.setTagId(circuitBreaker.run(() -> tagServiceClient.findTag(requestTag.getName(), auth).getId()));

        study.removeTag(tag);
        return study;
    }
}
