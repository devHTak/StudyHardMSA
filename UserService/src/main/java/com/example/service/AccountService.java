package com.example.service;

import com.example.entity.Account;
import com.example.entity.PasswordEntity;
import com.example.entity.SignUpEntity;
import com.example.entity.UpdateEntity;
import lombok.RequiredArgsConstructor;
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
    private final PasswordEncoder passwordEncoder;

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
}
