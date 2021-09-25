package com.example.service;

import com.example.entity.Address;
import com.example.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;


    public Zone saveZone(Address address) {
        Zone zone = new Zone();
        zone.setAddress(address);

        return zoneRepository.save(zone);
    }

    public boolean existsByAddress(Address address) {
        return zoneRepository.existsByAddress(address);
    }
}
