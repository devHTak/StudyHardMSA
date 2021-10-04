package com.example.service;

import com.example.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-service/members/{memberId}/studies")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<Study> saveStudy(@PathVariable String memberId, @Validated @RequestBody RequestStudy studyEntity, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(new Study());
        }

        Study returnStudy = studyService.saveStudy(studyEntity, memberId);
        return ResponseEntity.ok(returnStudy);
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<Study> findStudyByStudyId(@PathVariable String studyId, @PathVariable String memberId) {
        return ResponseEntity.ok(studyService.findByStudyId(studyId, memberId));
    }

    @GetMapping("/{studyId}/managers")
    public ResponseEntity<List<ResponseUser>> findManagersByStudyId(@PathVariable String studyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return ResponseEntity.ok(studyService.findManagersByStudyId(studyId, auth));
    }

    @GetMapping("/{studyId}/members")
    public ResponseEntity<List<ResponseUser>> findMembersByStudyId(@PathVariable String studyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return ResponseEntity.ok(studyService.findMembersByStudyId(studyId, auth));
    }

    @PostMapping("/{studyId}/join")
    public ResponseEntity<Boolean> joinStudyByMember(@PathVariable String studyId, @PathVariable String memberId) {
        try {
            return ResponseEntity.ok(studyService.joinStudy(studyId, memberId));
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/{studyId}/leave")
    public ResponseEntity<Boolean> leaveStudyByMember(@PathVariable String studyId, @PathVariable String memberId) {
        try {
            return ResponseEntity.ok(studyService.leaveStudy(studyId, memberId));
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/{studyId}/zones/add")
    public ResponseEntity<Study> addZone(@Valid @RequestBody RequestZone requestZone, BindingResult result
            , @PathVariable String studyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(studyService.addZone(studyId, requestZone, auth));
    }

    @PostMapping("/{studyId}/zones/remove")
    public ResponseEntity<Study> removeZone(@Valid @RequestBody RequestZone requestZone, BindingResult result
            , @PathVariable String studyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(studyService.removeZone(studyId, requestZone, auth));
    }

    @PostMapping("/{studyId}/tags/add")
    public ResponseEntity<Study> addTag(@Valid @RequestBody RequestTag requestTag, BindingResult result,
                                        @PathVariable String studyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(studyService.addTag(studyId, requestTag, auth));
    }

    @PostMapping("/{studyId}/tags/remove")
    public ResponseEntity<Study> removeTag(@Valid @RequestBody RequestTag requestTag, BindingResult result,
                                           @PathVariable String studyId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(studyService.removeTag(studyId, requestTag, auth));
    }
}
