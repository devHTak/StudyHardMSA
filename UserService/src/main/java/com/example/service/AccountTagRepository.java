package com.example.service;

import com.example.entity.account.AccountTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTagRepository extends JpaRepository<AccountTag, Long> {
    Optional<AccountTag> findByTagId(Long tagId);
}
