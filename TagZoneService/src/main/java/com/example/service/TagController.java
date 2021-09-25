package com.example.service;

import com.example.entity.Tag;
import com.example.entity.TagEntity;
import com.example.entity.TagValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag-zone-service/tags")
public class TagController {

    private final TagService tagService;
    private final TagValidator tagValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(tagValidator);
    }

    @PostMapping
    public ResponseEntity<Tag> saveTag(@Validated @RequestBody TagEntity tagName, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        Tag tag = tagService.save(tagName);
        return ResponseEntity.status(HttpStatus.CREATED).body(tag);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Boolean> existTagByName(@PathVariable String name) {
        return ResponseEntity.ok(tagService.existTag(name));
    }
}
