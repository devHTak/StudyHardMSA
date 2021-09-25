package com.example.service;

import com.example.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {


    Optional<Event> findByStudyIdAndEventIdAndUseFlag(String studyId, String eventId, boolean useFlag);

    List<Event> findByStudyIdAndUseFlag(String studyId, boolean useFLag);

    Optional<Event> findByEventIdAndUseFlag(String eventId, boolean useFlag);

}
