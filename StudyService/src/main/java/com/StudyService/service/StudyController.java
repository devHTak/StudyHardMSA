package com.StudyService.service;

import com.StudyService.domain.AccountEntity;
import com.StudyService.domain.Study;
import com.StudyService.domain.StudyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-service/studies")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<Study> saveStudy(@Validated @RequestBody StudyEntity studyEntity, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(new Study());
        }

        Study returnStudy = studyService.saveStudy(studyEntity);
        return ResponseEntity.ok(returnStudy);
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<Study> findStudyByStudyId(@PathVariable String studyId) {
        return ResponseEntity.ok(studyService.findByStudyId(studyId));
    }

    @GetMapping("/{studyId}/managers")
    public ResponseEntity<List<AccountEntity>> findManagersByStudyId(@PathVariable String studyId) {
        return ResponseEntity.ok(studyService.findManagersByStudyId(studyId));
    }

    @GetMapping("/{studyId}/members")
    public ResponseEntity<List<AccountEntity>> findMembersByStudyId(@PathVariable String studyId) {
        return ResponseEntity.ok(studyService.findMembersByStudyId(studyId));
    }

    @PostMapping("/{studyId}/members/{memberId}/join")
    public ResponseEntity<Boolean> joinStudyByMember(@PathVariable String studyId, @PathVariable String memberId) {
        try {
            return ResponseEntity.ok(studyService.joinStudy(studyId, memberId));
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/{studyId}/members/{memberId}/leave")
    public ResponseEntity<Boolean> leaveStudyByMember(@PathVariable String studyId, @PathVariable String memberId) {
        try {
            return ResponseEntity.ok(studyService.leaveStudy(studyId, memberId));
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
