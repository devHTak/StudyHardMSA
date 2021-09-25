package com.example.service;

import com.example.entity.Tag;
import com.example.entity.TagEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(value = Lifecycle.PER_CLASS)
class TagControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired TagRepository tagRepository;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("태그 생성 성공")
    @Order(0)
    void saveTagSuccessTest() throws Exception {
        TagEntity tagEntity = new TagEntity("Test");

        mockMvc.perform(post("/tag-zone-service/tags")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(tagEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.[?(@.name == '%s')]", tagEntity.getName()).exists());

        Tag tag = tagRepository.findByName(tagEntity.getName())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo(tagEntity.getName());
    }

    @Test
    @DisplayName("중복 명으로 태그 생성 실패")
    @Order(1)
    void saveTagFailByDuplicatedTest() throws Exception {
        TagEntity tagEntity = new TagEntity("Test");

        mockMvc.perform(post("/tag-zone-service/tags")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tagEntity)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Tag tag = tagRepository.findByName(tagEntity.getName())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(tag).isNotNull();
        assertThat(tag.getName()).isEqualTo(tagEntity.getName());
    }

    @Test
    @DisplayName("널값으로 태그 생성 실패")
    @Order(2)
    void saveTagFailNullTest() throws Exception {
        TagEntity tagEntity = new TagEntity();

        mockMvc.perform(post("/tag-zone-service/tags")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tagEntity)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("태그 확인 성공")
    @Order(3)
    void existByTagNameSuccessTest() throws Exception {
        mockMvc.perform(get("/tag-zone-service/tags/{name}", "Test"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}