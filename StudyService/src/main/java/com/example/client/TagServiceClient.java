package com.example.client;

import com.example.domain.RequestTag;
import com.example.domain.ResponseTag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "tag-service"
        , url = "http://localhost:8000"
        , contextId = "tag-service"
        , configuration = FeignErrorDecoder.class)
public interface TagServiceClient {

    @PostMapping("/tag-zone-service/tags")
    ResponseTag saveTag(@RequestBody RequestTag tag, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);

    @GetMapping("/tag-zone-service/tags/{name}")
    boolean existTag(@PathVariable String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);

    @GetMapping("/tag-zone-service/tags/{name}/find")
    ResponseTag findTag(@PathVariable String name, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth);
}
