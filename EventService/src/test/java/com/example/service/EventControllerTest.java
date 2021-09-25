package com.example.service;

import com.example.entity.Event;
import com.example.entity.EventEntity;
import com.example.entity.EventType;
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
public class EventControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired EventRepository eventRepository;
    @Autowired ObjectMapper objectMapper;
    private final String EVENT_ID1 = UUID.randomUUID().toString();
    private final String EVENT_ID2 = UUID.randomUUID().toString();
    private final String STUDY_ID1 = UUID.randomUUID().toString();
    private final String CREATED_BY = UUID.randomUUID().toString();

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
        event1.setCreatedBy(CREATED_BY);
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setEventId(EVENT_ID2);
        event2.setUseFlag(true);
        event2.setEventType(EventType.CONFIRMATIVE);
        event2.setTitle("EVENT2");
        event2.setDescription("test2");
        event2.setStudyId(STUDY_ID1);
        event2.setCreateDateTime(LocalDateTime.now());
        event2.setLimitOfEnrollments(5);
        event2.setCreatedBy(CREATED_BY);
        eventRepository.save(event2);
    }

    @AfterEach
    void afterEach() {
        eventRepository.deleteAll();
    }

    @Test
    @DisplayName("이벤트 생성 성공")
    void saveEventSuccessTest() throws Exception {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventType(EventType.FCFS);
        eventEntity.setTitle("TEST3");
        eventEntity.setLimitOfEnrollments(10);
        eventEntity.setStudyId(STUDY_ID1);
        eventEntity.setDescription("TEST3");

        String eventValue = objectMapper.writeValueAsString(eventEntity);

        mockMvc.perform(post("/event-service/members/{createdBy}/studies/{studyId}/events", CREATED_BY, STUDY_ID1)
                        .content(eventValue)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.title == '%s')]", "TEST3").exists());

        List<Event> events = eventRepository.findByStudyIdAndUseFlag(STUDY_ID1, true);
        assertThat(events.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이벤트 생성 실패")
    void saveEventFailTest() throws Exception {
        EventEntity eventEntity = new EventEntity();
        String eventValue = objectMapper.writeValueAsString(eventEntity);

        mockMvc.perform(post("/event-service/members/{createdBy}/studies/{studyId}/events", CREATED_BY, STUDY_ID1)
                        .content(eventValue)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());

        List<Event> events = eventRepository.findByStudyIdAndUseFlag(STUDY_ID1, true);
        assertThat(events.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("전체 이벤트 조회")
    void findAllEventTest() throws Exception {
        mockMvc.perform(get("/event-service/members/{createdBy}/studies/{studyId}/events", CREATED_BY, STUDY_ID1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.studyId == '%s')]", STUDY_ID1).exists());

        List<Event> events = eventRepository.findByStudyIdAndUseFlag(STUDY_ID1, true);
        assertThat(events.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("이벤트 조회")
    void findEventByIdTest() throws Exception {
        mockMvc.perform(get("/event-service/members/{createdBy}/studies/{studyId}/events/{eventId}", CREATED_BY, STUDY_ID1, EVENT_ID1)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.title == '%s')]", "EVENT1").exists());
    }

    @Test
    @DisplayName("이벤트 삭제")
    void deleteEventTest() throws Exception {
         mockMvc.perform(delete("/event-service/members/{createdBy}/studies/{studyId}/events/{eventId}", CREATED_BY, STUDY_ID1, EVENT_ID1)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.useFlag == false)]").exists());

        List<Event> usingEvents = eventRepository.findByStudyIdAndUseFlag(STUDY_ID1, true);
        assertThat(usingEvents.size()).isEqualTo(1);

        List<Event> notUsingEvents = eventRepository.findByStudyIdAndUseFlag(STUDY_ID1, false);
        assertThat(notUsingEvents.get(0).getTitle()).isEqualTo("EVENT1");
    }


}
