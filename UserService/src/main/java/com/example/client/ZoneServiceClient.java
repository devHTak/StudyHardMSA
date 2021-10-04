package com.example.client;

import com.example.entity.Address;
import com.example.entity.ResponseZone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "zone-service",
        url = "http://localhost:8000",
        contextId = "feignClientZone",
        configuration = FeignErrorDecoder.class)
public interface ZoneServiceClient {

    @GetMapping("/tag-zone-service/zones")
    ResponseEntity<Boolean> existZone(@RequestParam String city, @RequestParam String localNameCity, @RequestParam String province, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);

    @PostMapping("/tag-zone-service/zones")
    ResponseEntity<ResponseZone> addZone(@RequestBody Address address, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);

    @GetMapping("/tag-zone-service/zones/find")
    ResponseEntity<ResponseZone> findZone(@RequestParam String city, @RequestParam String localNameCity, @RequestParam String province, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);

    @DeleteMapping("/tag-zone-service/zones/{zoneId}")
    void deleteZoneByAId(@PathVariable Long zoneId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
