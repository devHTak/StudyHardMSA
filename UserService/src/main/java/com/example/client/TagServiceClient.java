package com.example.client;

import com.example.entity.zone_tag.RequestTag;
import com.example.entity.zone_tag.ResponseTag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "tag-service", url = "http://localhost:8000", contextId = "feignClientForTag", configuration = FeignErrorDecoder.class)
public interface TagServiceClient {

    @PostMapping("/tag-zone-service/tags")
    ResponseEntity<ResponseTag> saveTag(@RequestBody RequestTag tag, @RequestHeader(value=HttpHeaders.AUTHORIZATION) String token);

    @GetMapping("/tag-zone-service/tags/{name}")
    ResponseEntity<Boolean> existTag(@PathVariable String name, @RequestHeader(value=HttpHeaders.AUTHORIZATION) String token);

    @GetMapping("/tag-zone-service/tags/{name}/find")
    ResponseEntity<ResponseTag> findTag(@PathVariable String name, @RequestHeader(value=HttpHeaders.AUTHORIZATION) String token);

    @DeleteMapping("/tag-zone-service/tags/{name}/delete")
    ResponseEntity<Boolean> deleteTag(@PathVariable String name, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token);
}
