package com.example.service;

import com.example.entity.account.Account;
import com.example.entity.account.PasswordEntity;
import com.example.entity.account.SignUpEntity;
import com.example.entity.account.UpdateEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountService accountService;
    private static final String NICKNAME = "Test01";

    @BeforeEach
    void beforeEach() {
        SignUpEntity entity = new SignUpEntity();
        entity.setEmail("test01@test.com");
        entity.setNickname(NICKNAME);
        entity.setPassword("testtest1");

        accountService.signUp(entity);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 등록 실패")
    void signUpTestFail() throws Exception {
        SignUpEntity entity = new SignUpEntity();
        entity.setEmail("test@test.com");
        entity.setNickname("Test01");
        entity.setPassword("Test01");

        mockMvc.perform(post("/user-service/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entity)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 등록")
    void signUpTestSuccess() throws Exception {
        SignUpEntity entity = new SignUpEntity();
        entity.setEmail("test02@test.com");
        entity.setNickname("Test02");
        entity.setPassword("testtest2");

        mockMvc.perform(post("/user-service/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(entity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.email == '%s')]", "test02@test.com").exists());

        Account account = accountRepository.findByEmail("test02@test.com")
                .get();

        assertThat(account).isNotNull();
        assertThat(account.getNickname()).isEqualTo("Test02");
    }

    @Test
    @DisplayName("회원 닉네임 조회")
    void findAccountByNicknameTest() throws Exception {
        mockMvc.perform(get("/user-service/profiles/{nickname}", NICKNAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.email == '%s')]", "test01@test.com").exists());

        Account account = accountRepository.findByNickname(NICKNAME)
                .get();

        assertThat(account).isNotNull();
        assertThat(account.getNickname()).isEqualTo("Test01");
    }

    @Test
    @DisplayName("회원 수정")
    void updateProfilesTest() throws Exception {
        UpdateEntity entity = new UpdateEntity();
        entity.setBio("BIO");
        entity.setOccupation("Occupation");
        entity.setLocation("Location");
        entity.setUrl("URL");

        mockMvc.perform(put("/user-service/profiles/{nickname}", NICKNAME)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(entity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.email == '%s' )]", "test01@test.com").exists());
    }

    @Test
    @DisplayName("패스워드 수정")
    void updatePasswordTest() throws Exception {
        PasswordEntity entity = new PasswordEntity();
        entity.setEmail("test02@test.com");
        entity.setPassword("testtest01");
        entity.setNickname(NICKNAME);

        mockMvc.perform(put("/user-service/profiles/{nickname}/password", NICKNAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.email == '%s' )]", "test02@test.com").exists());
    }
}