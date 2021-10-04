package com.example.service;

import com.example.entity.Address;
import com.example.entity.Zone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoneService {

    private final ZoneRepository zoneRepository;


    public Zone saveZone(Address address) {
        if(this.existsByAddress(address)){
            return zoneRepository.findByAddressCityAndAddressLocalNameCityAndAddressProvince(
                    address.getCity(), address.getLocalNameCity(), address.getProvince())
                    .orElseThrow(IllegalArgumentException::new);
        }
        Zone zone = new Zone();
        zone.setAddress(address);
        log.info("save: {}, {}, {}", address.getCity(), address.getLocalNameCity(), address.getProvince());
        return zoneRepository.save(zone);
    }

    public boolean existsByAddress(Address address) {
        log.info("exist: {}, {}, {}", address.getCity(), address.getLocalNameCity(), address.getProvince());
        return zoneRepository.existsByAddressCityAndAddressLocalNameCityAndAddressProvince(address.getCity(), address.getLocalNameCity(), address.getProvince());
    }

    public Zone findByAddress(Address address) {
        return zoneRepository.findByAddressCityAndAddressLocalNameCityAndAddressProvince(address.getCity(), address.getLocalNameCity(), address.getProvince())
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean deleteZoneById(long zoneId) {
        zoneRepository.deleteById(zoneId);
        return true;
    }

    public Zone findZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
