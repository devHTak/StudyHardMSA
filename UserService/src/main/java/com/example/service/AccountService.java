package com.example.service;

import com.example.client.TagServiceClient;
import com.example.client.ZoneServiceClient;
import com.example.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountTagRepository accountTagRepository;
    private final AccountZoneRepository accountZoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagServiceClient tagServiceClient;
    private final ZoneServiceClient zoneServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);

        return new User(account.getEmail(), account.getPassword(), new ArrayList<>());
    }

    public Account getUserByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Account signUp(SignUpEntity signUpEntity) {
        Account account = new Account();
        account.setEmail(signUpEntity.getEmail());
        account.setPassword(passwordEncoder.encode(signUpEntity.getPassword()));
        account.setNickname(signUpEntity.getNickname());
        account.setCreatedAt(LocalDateTime.now());
        account.setUserId(UUID.randomUUID().toString());

        return accountRepository.save(account);
    }

    public Account findAccountByNickname(String nickname) {
        return accountRepository.findByNickname(nickname)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Account updateAccountByNickname(String nickname, UpdateEntity updateEntity) {
        Account account = accountRepository.findByNickname(nickname)
                .orElseThrow(IllegalArgumentException::new);

        account.setBio(updateEntity.getBio());
        account.setLocation(updateEntity.getLocation());
        account.setOccupation(updateEntity.getOccupation());
        account.setUrl(updateEntity.getUrl());

        return account;
    }

    public Account updatePassword(String nickname, PasswordEntity passwordEntity) {
        Account account = accountRepository.findByNickname(nickname)
                .orElseThrow(IllegalArgumentException::new);
        account.setPassword(passwordEncoder.encode(passwordEntity.getPassword()));
        account.setEmail(passwordEntity.getEmail());
        account.setNickname(passwordEntity.getNickname());

        return account;
    }

    public Account findAccountByUserId(String userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Account saveTag(RequestTag requestTag, String userId, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);

        AccountTag tag = new AccountTag();
        if(circuitBreaker.run(() -> tagServiceClient.existTag(requestTag.getName(), auth)).getBody()) {
            tag.setTagId(circuitBreaker.run(() -> tagServiceClient.findTag(requestTag.getName(), auth)).getBody().getId());
        } else {
            tag.setTagId(circuitBreaker.run(() -> tagServiceClient.saveTag(requestTag, auth)).getBody().getId());
        }

        account.addTag(tag);
        accountTagRepository.save(tag);
        return account;
    }

    public Account removeTag(RequestTag requestTag, String userId, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);

        Long tagId = circuitBreaker.run(() -> tagServiceClient.findTag(requestTag.getName(), auth)).getBody().getId();
        AccountTag accountTag = accountTagRepository.findByTagId(tagId)
                .orElseThrow(IllegalArgumentException::new);
        account.removeTag(accountTag);
        return account;
    }

    public Account saveZone(RequestZone requestZone, String userId, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);

        AccountZone zone = new AccountZone();
        if(circuitBreaker.run(() -> zoneServiceClient.existZone(requestZone.getAddress().getCity()
                ,requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth)).getBody()) {
            zone.setZoneId(circuitBreaker.run(() -> zoneServiceClient.findZone(requestZone.getAddress().getCity()
                    ,requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth)).getBody().getId());
        } else {
            zone.setZoneId(circuitBreaker.run(() -> zoneServiceClient.addZone(requestZone.getAddress(), auth)).getBody().getId());
        }

        account.addZone(zone);
        accountZoneRepository.save(zone);
        return account;
    }

    public Account removeZone(RequestZone requestZone, String userId, String auth) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(IllegalArgumentException::new);

        long zoneId = circuitBreaker.run(() ->zoneServiceClient.findZone(requestZone.getAddress().getCity()
                ,requestZone.getAddress().getLocalNameCity(), requestZone.getAddress().getProvince(), auth)).getBody().getId();
        AccountZone zone = accountZoneRepository.findByZoneId(zoneId)
                        .orElseThrow(IllegalArgumentException::new);
        account.removeZone(zone);

        return account;
    }
}
