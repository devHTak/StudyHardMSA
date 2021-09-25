package com.example.service;

import com.example.domain.AccountEntity;
import com.example.domain.Study;
import com.example.domain.StudyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-service/members/{memberId}/studies")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<Study> saveStudy(@PathVariable String memberId, @Validated @RequestBody StudyEntity studyEntity, BindingResult result) {
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
    public ResponseEntity<List<AccountEntity>> findManagersByStudyId(@PathVariable String studyId) {
        return ResponseEntity.ok(studyService.findManagersByStudyId(studyId));
    }

    @GetMapping("/{studyId}/members")
    public ResponseEntity<List<AccountEntity>> findMembersByStudyId(@PathVariable String studyId) {
        return ResponseEntity.ok(studyService.findMembersByStudyId(studyId));
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
}
