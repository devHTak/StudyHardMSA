package com.example.service;

import com.example.client.TagServiceClient;
import com.example.client.UserServiceClient;
import com.example.client.ZoneServiceClient;
import com.example.domain.*;
import com.example.mq.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyManagerRepository studyManagerRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyTagRepository studyTagRepository;
    private final StudyZoneRepository studyZoneRepository;
    private final UserServiceClient userServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final ZoneServiceClient zoneServiceClient;
    private final TagServiceClient tagServiceClient;
    private final KafkaProducer kafkaProducer;

    @Value("${kafka.topic.notification}")
    private String notificationTopic;

    public Study saveStudy(RequestStudy studyEntity, String managerId) {
        Study study = new Study();
        study.setTitle(studyEntity.getTitle());
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
        Study study = studyRepository.findByStudyId(studyId).orElseThrow(IllegalArgumentException::new);

        log.info(studyId);
        log.info(requestZone.getAddress().getCity());
        log.info(study.getTitle());

        // zone api by feign client
        StudyZone zone = new StudyZone();
        ResponseZone responseZone = null;
        if(!circuitBreaker.run(()-> zoneServiceClient.existZone(requestZone.getAddress().getCity()
                ,requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth), e -> {
            log.error("Zone Service Client existZone: {}", e.getMessage());
            return false;
        })) {
            responseZone = circuitBreaker.run(()-> zoneServiceClient.addZone(requestZone.getAddress()), e -> {
                log.error("Zone Service Client addZone: {}", e.getMessage());
                return new ResponseZone();
            });
            zone.setZoneId(responseZone.getId());
        } else {
            responseZone = circuitBreaker.run(() -> zoneServiceClient.findZone(requestZone.getAddress().getCity()
                    , requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth), e-> {
                log.error("Zone Service Client findZone: {}", e.getMessage());
                return new ResponseZone();
            });
            zone.setZoneId(responseZone.getId());
        }

        // kafka send message
        log.info(study.getTitle());
        log.info(responseZone.getAddress().getCity());
        String message = String.format("%s created at %s, %s, %s", study.getTitle(), responseZone.getAddress().getCity(), responseZone.getAddress().getLocalNameCity(), responseZone.getAddress().getProvince());
        sendKafkaMessage(studyId, responseZone, null, message);

        study.addZone(zone);
        studyZoneRepository.save(zone);
        return study;
    }

    public Study removeZone(String studyId, RequestZone requestZone, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyId(studyId).orElseThrow(IllegalArgumentException::new);

        // zone api by feign client
        ResponseZone responseZone = circuitBreaker.run(()->zoneServiceClient.findZone(
                requestZone.getAddress().getCity(), requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth), e -> {
            log.error("Zone Service Client findZone: {}", e.getMessage());
            return new ResponseZone();
        });
        StudyZone zone = studyZoneRepository.findByZoneId(responseZone.getId()).orElseThrow(IllegalArgumentException::new);

        // kafka send message
        String message = String.format("%s removed at %s, %s, %s", study.getTitle(), responseZone.getAddress().getCity(), responseZone.getAddress().getLocalNameCity(), responseZone.getAddress().getProvince());
        sendKafkaMessage(studyId, responseZone, null, message);

        study.removeZone(zone);
        return study;
    }

    public Study addTag(String studyId, RequestTag requestTag, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyId(studyId).orElseThrow(IllegalArgumentException::new);

        // tag api by feign client
        StudyTag tag = new StudyTag();
        ResponseTag responseTag = new ResponseTag();
        if(!circuitBreaker.run(()-> tagServiceClient.existTag(requestTag.getName(), auth), e -> {
            log.error("Tag Service Client existTag: {}", e.getMessage());
            return false;
        })) {
            responseTag = circuitBreaker.run(() -> tagServiceClient.saveTag(requestTag, auth), e -> {
                log.error("Tag Service Client saveTag: {}", e.getMessage());
                return new ResponseTag();
            });
            tag.setTagId(responseTag.getId());
        } else {
            responseTag = circuitBreaker.run(() -> tagServiceClient.findTag(requestTag.getName(), auth), e-> {
                log.error("Tag Service Client findTag: {}", e.getMessage());
                return new ResponseTag();
            });
            tag.setTagId(responseTag.getId());
        }

        // Kafka Send message
        String message = String.format("%s created in %s", study.getTitle(), responseTag.getName());
        sendKafkaMessage(studyId, null, responseTag, message);

        study.addTag(tag);
        studyTagRepository.save(tag);
        return study;
    }

    public Study removeTag(String studyId, RequestTag requestTag, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Study study = studyRepository.findByStudyIdWithTag(studyId);

        // tag api by feign client
        ResponseTag responseTag = circuitBreaker.run(() -> tagServiceClient.findTag(requestTag.getName(), auth), e-> {
            log.error("Tag Service Client findTag: {}", e.getMessage());
            return new ResponseTag();
        });
        StudyTag tag = studyTagRepository.findByTagId(responseTag.getId()).orElseThrow(IllegalArgumentException::new);

        // Kafka send message
        String message = String.format("%s removed in %s", study.getTitle(), responseTag.getName());
        sendKafkaMessage(studyId, null, responseTag, message);

        study.removeTag(tag);
        return study;
    }

    private void sendKafkaMessage(String studyId, ResponseZone responseZone, ResponseTag responseTag, String message) {
        Notification notification = new Notification();
        notification.setStudyId(studyId);
        notification.setZone(responseZone);
        notification.setTag(responseTag);
        notification.setMessage(message);

        log.info("Kafka Topic: {}", notificationTopic);
        kafkaProducer.sendNotification(notificationTopic, notification);
    }
}
