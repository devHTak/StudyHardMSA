package com.example.service;

import com.example.entity.Address;
import com.example.entity.Tag;
import com.example.entity.TagEntity;
import com.example.entity.Zone;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(value = Lifecycle.PER_CLASS)
class ZoneControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ZoneRepository zoneRepository;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("태그 생성 성공")
    @Order(0)
    void saveTagSuccessTest() throws Exception {
        Address address = new Address("Test1", "Test2", "Test3");

        mockMvc.perform(post("/tag-zone-service/zones")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(address)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.[?(@.address.city == '%s')]", address.getCity()).exists())
                .andExpect(jsonPath("$.[?(@.address.localNameCity == '%s')]", address.getLocalNameCity()).exists())
                .andExpect(jsonPath("$.[?(@.address.province == '%s')]", address.getProvince()).exists());

        Zone zone = zoneRepository.findByAddress(address)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(zone).isNotNull();
        assertThat(zone.getAddress()).isEqualTo(address);
    }

    @Test
    @DisplayName("중복 명으로 태그 생성 실패")
    @Order(1)
    void saveTagFailByDuplicatedTest() throws Exception {
        Address address = new Address("Test1", "Test2", "Test3");

        mockMvc.perform(post("/tag-zone-service/zones")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(address)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Zone zone = zoneRepository.findByAddress(address)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(zone).isNotNull();
        assertThat(zone.getAddress()).isEqualTo(address);
    }

    @Test
    @DisplayName("널값으로 태그 생성 실패")
    @Order(2)
    void saveTagFailNullTest() throws Exception {
        Address address = new Address();

        mockMvc.perform(post("/tag-zone-service/zones")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(address)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("태그 확인 성공")
    @Order(3)
    void existByTagNameSuccessTest() throws Exception {
        Address address = new Address("Test1", "Test2", "Test3");
        mockMvc.perform(get("/tag-zone-service/zones")
                        .param("city", address.getCity())
                        .param("localNameCity", address.getLocalNameCity())
                        .param("province", address.getProvince()))
                .andDo(print())
                .andExpect(status().isOk());
    }

}