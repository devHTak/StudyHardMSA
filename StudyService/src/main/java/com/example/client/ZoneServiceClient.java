package com.example.client;

import com.example.domain.RequestZone;
import com.example.domain.ResponseZone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "zone-service"
        , url = "http://localhost:8080"
        , contextId = "zone-service"
        , configuration = FeignErrorDecoder.class)
public interface ZoneServiceClient {

    @GetMapping("/tag-zone-service/zones")
    boolean existZone(@RequestParam String city, @RequestParam String localNameCity, @RequestParam String province,
                      @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);

    @PostMapping("/tag-zone-service/zones")
    ResponseZone addZone(@RequestBody RequestZone zone);

    @GetMapping("/tag-zone-service/zones/find")
    ResponseZone findZone(@RequestParam String city, @RequestParam String localNameCity, @RequestParam String province,
                          @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);
}
