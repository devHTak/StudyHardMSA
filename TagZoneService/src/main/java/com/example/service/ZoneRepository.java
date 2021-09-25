package com.example.service;

import com.example.entity.Address;
import com.example.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByAddress(Address address);

    Optional<Zone> findByAddress(Address address);
}
