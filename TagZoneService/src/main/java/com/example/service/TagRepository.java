package com.example.service;

import com.example.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String tagName);

    Optional<Tag> findByName(String name);
}
