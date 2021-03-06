package com.example.service;

import com.example.entity.Enrollment;
import com.example.entity.Event;
import com.example.entity.EventType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Slf4j
public class EnrollmentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired EventRepository eventRepository;
    private final String EVENT_ID1 = UUID.randomUUID().toString();
    private final String STUDY_ID1 = UUID.randomUUID().toString();
    private final String MEMBER_ID = UUID.randomUUID().toString();
    private long enrollmentId;

    @BeforeEach
    void beforeEach() {
        Event event1 = new Event();
        event1.setEventId(EVENT_ID1);
        event1.setUseFlag(true);
        event1.setEventType(EventType.CONFIRMATIVE);
        event1.setTitle("EVENT1");
        event1.setDescription("test1");
        event1.setStudyId(STUDY_ID1);
        event1.setCreateDateTime(LocalDateTime.now());
        event1.setLimitOfEnrollments(5);
        event1.setCreatedBy(MEMBER_ID);

        Enrollment enrollment = new Enrollment();
        enrollment.setMemberId(MEMBER_ID);
        enrollment.setAccepted(false);
        enrollment.setAttended(false);
        enrollment.setUseFlag(true);

        event1.addEnrollment(enrollment);
        eventRepository.save(event1);
        Enrollment returnEnrollment = enrollmentRepository.save(enrollment);
        enrollmentId = returnEnrollment.getId();
    }
    @AfterEach
    void afterEach() {
        enrollmentRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @DisplayName("????????? ??????")
    void enrollmentTest() throws Exception {
        mockMvc.perform(post("/event-service/members/{memberId}/events/{eventId}/enrollments/enroll", MEMBER_ID, EVENT_ID1))
                .andDo(print())
                .andExpect(status().isOk());

        Event event = eventRepository.findByEventIdAndUseFlag(EVENT_ID1, true)
                .orElseThrow(IllegalArgumentException::new);
        List<Enrollment> enrollments = enrollmentRepository.findByEventId(event.getId());
        assertThat(enrollments.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void disenrollmentTest() throws Exception, IllegalArgumentException {
        mockMvc.perform(post("/event-service/members/{memberId}/events/{eventId}/enrollments/disenroll", MEMBER_ID, EVENT_ID1)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        Event event = eventRepository.findByEventIdAndUseFlag(EVENT_ID1, true)
                .orElseThrow(IllegalArgumentException::new);
        List<Enrollment> enrollments = enrollmentRepository.findByEventId(event.getId());
        assertThat(enrollments.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("?????? Accept")
    void acceptEnrollmentTest() throws Exception {
        mockMvc.perform(post("/event-service/members/{memberId}/events/{eventId}/enrollments/{enrollmentId}/accept", MEMBER_ID, EVENT_ID1, enrollmentId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.accepted == true)]").exists());

        Enrollment enrollments = enrollmentRepository.findById(enrollmentId)
                .orElse(new Enrollment());
        assertThat(enrollments.isAccepted()).isTrue();
    }

    @Test
    @DisplayName("?????? Reject")
    void rejectEnrollmentTest() throws Exception {
        mockMvc.perform(post("/event-service/members/{memberId}/events/{eventId}/enrollments/{enrollmentId}/reject", MEMBER_ID, EVENT_ID1, enrollmentId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.accepted == false)]").exists());

        Enrollment enrollments = enrollmentRepository.findById(enrollmentId)
                .orElse(new Enrollment());
        assertThat(enrollments.isAccepted()).isFalse();
    }

    @Test
    @DisplayName("Check In")
    void checkInEnrollmentTest() throws Exception {
        mockMvc.perform(post("/event-service/members/{memberId}/events/{eventId}/enrollments/{enrollmentId}/check-in", MEMBER_ID, EVENT_ID1, enrollmentId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.attended == true)]").exists());

        Enrollment enrollments = enrollmentRepository.findById(enrollmentId)
                .orElse(new Enrollment());
        assertThat(enrollments.isAttended()).isTrue();
    }

    @Test
    @DisplayName("Cancel Check In")
    void cancelCheckInEnrollmentTest() throws Exception {
        mockMvc.perform(post("/event-service/members/{memberId}/events/{eventId}/enrollments/{enrollmentId}/cancel-check-in", MEMBER_ID, EVENT_ID1, enrollmentId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.attended == false)]").exists());

        Enrollment enrollments = enrollmentRepository.findById(enrollmentId)
                .orElse(new Enrollment());
        assertThat(enrollments.isAttended()).isFalse();
    }
}
