package com.example.service;

import com.example.domain.Study;
import com.example.domain.StudyEntity;
import com.example.domain.StudyManager;
import com.example.domain.StudyMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class StudyControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired StudyRepository studyRepository;
    @Autowired StudyManagerRepository studyManagerRepository;
    @Autowired StudyMemberRepository studyMemberRepository;
    @Autowired ObjectMapper objectMapper;

    private final static String STUDY_ID = UUID.randomUUID().toString();
    private final static String MEMBER_ID = UUID.randomUUID().toString();
    private final static String MANAGER_ID = UUID.randomUUID().toString();
    private final static String CREATED_BY = UUID.randomUUID().toString();

    @BeforeEach
    void beforeEach() {
        Study study = new Study();
        study.setImage("TEST11");
        study.setPath("TEST12");
        study.setFullDescription("TEST13");
        study.setShortDiscription("TEST14");
        study.setUseBanner(true);
        study.setStudyId(STUDY_ID);
        study.setCreatedBy(CREATED_BY);

        Study save = studyRepository.save(study);
        System.out.println(save.getId() + " " + study.getStudyId());

        StudyManager studyManager = new StudyManager();
        studyManager.setStudy(save);
        studyManager.setManagerId(MANAGER_ID);
        studyManager.setUseFlag(true);
        StudyManager returnValue = studyManagerRepository.save(studyManager);
        System.out.println(returnValue.getId() + " " + returnValue.getStudy().getId() + " " + returnValue.getManagerId());

        StudyMember studyMember = new StudyMember();
        studyMember.setStudy(save);
        studyMember.setMemberId(MEMBER_ID);
        studyMember.setUseFlag(true);
        StudyMember save1 = studyMemberRepository.save(studyMember);
        System.out.println(save1.getId() + " studyMember");
    }

    @AfterEach
    void afterEach() {
        studyManagerRepository.deleteAll();
        studyMemberRepository.deleteAll();
        studyRepository.deleteAll();
    }

    @Test
    @DisplayName("스터디 생성-success")
    void saveStudyFormSuccess() throws Exception {
        StudyEntity studyEntity = new StudyEntity();
        studyEntity.setImage("TEST21");
        studyEntity.setPath("TEST22");
        studyEntity.setTitle("TEST23");
        studyEntity.setFullDescription("TEST33");
        studyEntity.setShortDiscription("TEST44");
        studyEntity.setUseBanner(true);
        String studyFormJson = objectMapper.writeValueAsString(studyEntity);
        mockMvc.perform(post("/study-service/members/{createdBy}/studies", CREATED_BY)
                        .content(studyFormJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
        List<Study> studies = studyRepository.findAll();

        assertThat(studies.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("스터디 생성-fail")
    void saveStudyFormFail() throws Exception {
        StudyEntity studyEntity = new StudyEntity();
        studyEntity.setImage("TEST21");
        studyEntity.setPath("TEST22");
        studyEntity.setTitle(null);
        studyEntity.setFullDescription("TEST33");
        studyEntity.setShortDiscription("TEST44");
        studyEntity.setUseBanner(true);
        String studyFormJson = objectMapper.writeValueAsString(studyEntity);
        mockMvc.perform(post("/study-service/members/{createdBy}/studies", CREATED_BY)
                        .content(studyFormJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());

        List<Study> studies = studyRepository.findAll();
        assertThat(studies.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Member 조회")
    void findMemberTest() throws Exception {
        mockMvc.perform(get("/study-service/members/{createdBy}/studies/{studyId}/members", CREATED_BY, STUDY_ID))
                .andDo(print())
                .andExpect(status().isOk());

        Study study = studyRepository.findByStudyId(STUDY_ID).orElseThrow(IllegalArgumentException::new);
        List<StudyMember> studyMembers = studyMemberRepository.findByStudyAndUseFlag(study, true);

        assertThat(studyMembers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Manager 조회")
    void findManagerTest() throws Exception {
        mockMvc.perform(get("/study-service/members/{createdBy}/studies/{studyId}/managers", CREATED_BY, STUDY_ID))
                .andDo(print())
                .andExpect(status().isOk());

        Study study = studyRepository.findByStudyId(STUDY_ID).orElseThrow(IllegalArgumentException::new);
        List<StudyManager> studyManagers = studyManagerRepository.findByStudyAndUseFlag(study, true);

        assertThat(studyManagers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Member 등록")
    void findMemberJoinTest() throws Exception {
        Study study = studyRepository.findByStudyId(STUDY_ID).orElseThrow(IllegalArgumentException::new);
        String memberId = UUID.randomUUID().toString();

        StudyMember studyMember = new StudyMember();
        studyMember.setStudy(study);
        studyMember.setMemberId(memberId);
        mockMvc.perform(post("/study-service/members/{memberId}/studies/{studyId}/join", memberId, STUDY_ID))
                .andDo(print())
                .andExpect(status().isOk());

        List<StudyMember> studyMembers = studyMemberRepository.findByStudyAndUseFlag(study, true);

        assertThat(studyMembers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Member 탈퇴")
    void findManagerLeaveTest() throws Exception {
        Study study = studyRepository.findByStudyId(STUDY_ID).orElseThrow(IllegalArgumentException::new);

        mockMvc.perform(post("/study-service/members/{memberId}/studies/{studyId}/leave", MEMBER_ID, STUDY_ID))
                .andDo(print())
                .andExpect(status().isOk());

        List<StudyMember> studyMembers = studyMemberRepository.findByStudyAndUseFlag(study, true);
        assertThat(studyMembers.size()).isEqualTo(0);
    }

}
