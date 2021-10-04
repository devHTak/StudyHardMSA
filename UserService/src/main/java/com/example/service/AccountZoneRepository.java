package com.example.service;

import com.example.entity.AccountZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountZoneRepository extends JpaRepository<AccountZone, Long> {
    Optional<AccountZone> findByZoneId(long zoneId);
}
