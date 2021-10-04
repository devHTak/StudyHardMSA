package com.example.client;

import com.example.entity.Account;
import com.example.entity.RequestLogin;
import com.example.entity.RequestTag;
import com.example.entity.ResponseTag;
import com.example.service.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TagServiceClientTest {

    @Autowired TestRestTemplate testRestTemplate;
    @Autowired TagServiceClient tagServiceClient;
    @Autowired ZoneServiceClient zoneServiceClient;
    @Autowired AccountRepository accountRepository;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    private String token;


    @BeforeEach
    void beforeEach() throws Exception {

        // Authorization
        Account account = new Account();
        account.setEmail("test@test.com");
        account.setPassword("test1234");
        account.setNickname("test");
        Account returnValue = accountRepository.save(account);

        RequestLogin login = new RequestLogin();
        login.setEmail("test@test.com");
        login.setPassword("test1234");
        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        token = "Bearer " + mvcResult.getResponse().getHeader("token");

        // Tag insert
        RequestTag requestTag = new RequestTag();
        requestTag.setName("Test");
        tagServiceClient.saveTag(requestTag, token);
    }

    @AfterEach
    void afterEach() {
        tagServiceClient.deleteTag("Test", token);
        if(tagServiceClient.existTag("Test1", token).getBody()) {
            tagServiceClient.deleteTag("Test1", token);
        }
        if(tagServiceClient.existTag("Test2", token).getBody()) {
            tagServiceClient.deleteTag("Test2", token);
        }

        if(tagServiceClient.existTag("Test22", token).getBody()) {
            tagServiceClient.deleteTag("Test22", token);
        }
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("restTemplate으로 Tag 조회")
    void restTemplateTagTest() {
        RequestTag requestTag = new RequestTag();
        requestTag.setName("Test1");

        String uri = "http://localhost:8000/tag-zone-service/tags";
        // Authorization token setting
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<RequestTag> request = new HttpEntity<>(requestTag, headers);
        ResponseEntity<ResponseTag> responseEntity = testRestTemplate.postForEntity(uri, request, ResponseTag.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Feign - Tag 생성 테스트")
    void saveTagFeignTest() {
        RequestTag requestTag = new RequestTag();
        requestTag.setName("Test2");

        // Header 를 넣어서 테스트 하기 위해 새로운 메서드 형성, @RquestHeader(value=HttpMethod.AUTHORIZATION) String token
        ResponseTag responseTag = tagServiceClient.saveTag(requestTag, token).getBody();

        assertThat(responseTag.getName()).isEqualTo(requestTag.getName());
        assertThat(responseTag.getId()).isNotNull();
    }

    @Test
    @DisplayName("Feign - Tag 조회 테스트")
    void findTagFeignTest() {
        ResponseTag responseTag = tagServiceClient.findTag("Test", token)
                .getBody();
        assertThat(responseTag.getId()).isNotNull();
        assertThat(responseTag.getName()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Feign - Tag 확인 성공 테스트")
    void existTagFeignTrueTest() {
        boolean test = tagServiceClient.existTag("Test", token).getBody();
        assertThat(test).isTrue();
    }

    @Test
    @DisplayName("Feign - Tag 확인 실패 테스트")
    void existTagFeignFalseTest() {
        boolean test = tagServiceClient.existTag("Test22", token).getBody();
        assertThat(test).isFalse();
    }
}