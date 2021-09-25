package com.example.service;

import com.example.entity.Tag;
import com.example.entity.TagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    public Tag save(TagEntity tagEntity) {
        Tag tag = new Tag();
        tag.setName(tagEntity.getName());

        return tagRepository.save(tag);
    }

    public boolean existTag(String tagName) {
        return tagRepository.existsByName(tagName);
    }
}
