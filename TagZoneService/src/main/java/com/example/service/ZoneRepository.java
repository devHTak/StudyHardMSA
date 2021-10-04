package com.example.service;

import com.example.entity.Address;
import com.example.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    boolean existsByAddressCityAndAddressLocalNameCityAndAddressProvince(String city, String localNameCity, String province);

    Optional<Zone> findByAddressCityAndAddressLocalNameCityAndAddressProvince(String city, String localNameCity, String province);
}
