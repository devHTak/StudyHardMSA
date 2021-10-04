package com.example.client;

import com.example.entity.*;
import com.example.service.AccountRepository;
import com.example.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class ZoneServiceClientTest {

    @Autowired MockMvc mockMvc;
    @Autowired ZoneServiceClient zoneServiceClient;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @Autowired ObjectMapper objectMapper;
    @Autowired TestRestTemplate testRestTemplate;
    private String token;
    private long id;

    @BeforeEach
    void beforeEach() throws Exception {
        SignUpEntity entity = new SignUpEntity();
        entity.setEmail("test@test.com");
        entity.setNickname("test");
        entity.setPassword("testtest1");

        accountService.signUp(entity);
        RequestLogin login = new RequestLogin();
        login.setPassword("testtest1");
        login.setEmail("test@test.com");

        token = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getHeader("token");

        Address address = new Address("test2", "test2", "test2");
        id = zoneServiceClient.addZone(address, token).getBody().getId();
    }

    @AfterEach
    void afterEach() {
        zoneServiceClient.deleteZoneByAId(id, token);
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("RestTemplate 생성 테스트")
    void restTemplateTest() {
        String uri = "http://localhost:8000/tag-zone-service/zones";

        Address address = new Address("test1", "test1", "test1");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<Address> httpEntity = new HttpEntity(address, headers);
        ResponseZone zone = testRestTemplate.postForEntity(uri, httpEntity, ResponseZone.class).getBody();

        assertThat(zone.getAddress().getCity()).isEqualTo("test1");

        uri += "/" + zone.getId();
        testRestTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
    }

    @Test
    @DisplayName("Feign - Zone 생성 테스트")
    void saveZoneTestByFeign() {
        Address address = new Address("test3", "test3", "test3");

        ResponseZone responseZone = zoneServiceClient.addZone(address, token).getBody();
        assertThat(responseZone.getId()).isNotNull();
        assertThat(responseZone.getAddress().getCity()).isEqualTo("test3");

        // 삭제
        zoneServiceClient.deleteZoneByAId(responseZone.getId(), token);
    }

    @Test
    @DisplayName("Feign - Zone 조회 테스트")
    void findZoneTestByFeign() {
        Address address = new Address("test2", "test2", "test2");
        ResponseZone responseZone = zoneServiceClient.findZone(
                address.getCity(), address.getLocalNameCity(), address.getProvince(), token).getBody();

        assertThat(responseZone.getId()).isNotNull();
        assertThat(responseZone.getAddress().getCity()).isEqualTo(address.getCity());
    }

    @Test
    @DisplayName("Feign - Zone 확인 성공 테스트")
    void existZoneTrueTestByFeign() {
        Address address = new Address("test2", "test2", "test2");
        boolean result = zoneServiceClient.existZone(
                address.getCity(), address.getLocalNameCity(), address.getProvince(), token).getBody();
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Feign - Zone 확인 실패 테스트")
    void existZoneFailTestByFeign() {
        Address address = new Address("test3", "test3", "test3");
        boolean result = zoneServiceClient.existZone(
                address.getCity(), address.getLocalNameCity(), address.getProvince(), token).getBody();
        assertThat(result).isFalse();
    }

}