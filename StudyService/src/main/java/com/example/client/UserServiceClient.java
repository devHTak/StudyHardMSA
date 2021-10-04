package com.example.client;

import com.example.domain.ResponseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service"
        , url = "http://localhost:8080"
        , contextId = "user-service"
        , configuration=FeignErrorDecoder.class)
public interface UserServiceClient {

    @GetMapping("/user-service/users/{userId}")
    ResponseUser getUser(@PathVariable String userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);
}
