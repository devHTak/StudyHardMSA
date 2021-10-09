package com.example.service;

import com.example.entity.account.Account;
import com.example.entity.account.PasswordEntity;
import com.example.entity.account.SignUpEntity;
import com.example.entity.account.UpdateEntity;
import com.example.entity.validate.SignUpValidator;
import com.example.entity.zone_tag.RequestTag;
import com.example.entity.zone_tag.RequestZone;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service")
public class AccountController {

    private final AccountService accountService;
    private final SignUpValidator signUpValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(signUpValidator);
    }


    @PostMapping("/sign-up")
    public ResponseEntity<Account> signUp(@Validated @RequestBody SignUpEntity signUpEntity, BindingResult result) {
        if(result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(null);
        }

        Account returnAccount = accountService.signUp(signUpEntity);

        return ResponseEntity.ok(returnAccount);
    }

    @GetMapping("/profiles/{nickname}")
    public ResponseEntity<Account> findProfilesByNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(accountService.findAccountByNickname(nickname));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Account> findUserByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(accountService.findAccountByUserId(userId));
    }

    @PostMapping("/users/{userId}/tag/add")
    public ResponseEntity<Account> saveTag(@Validated @RequestBody RequestTag tag, BindingResult result,
                                           @PathVariable String userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(accountService.saveTag(tag, userId, auth));
    }

    @PostMapping("/users/{userId}/tag/remove")
    public ResponseEntity<Account> removeTag(@Validated @RequestBody RequestTag tag, BindingResult result,
                                             @PathVariable String userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(accountService.removeTag(tag, userId, auth));
    }

    @PostMapping("/users/{userId}/zone/add")
    public ResponseEntity<Account> saveZone(@Validated @RequestBody RequestZone zone, BindingResult result,
                                            @PathVariable String userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(accountService.saveZone(zone, userId, auth));
    }

    @PostMapping("/users/{userId}/zone/remove")
    public ResponseEntity<Account> removeZone(@Validated @RequestBody RequestZone zone, BindingResult result,
                                              @PathVariable String userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(accountService.removeZone(zone, userId, auth));
    }

    @PutMapping("/profiles/{nickname}")
    public ResponseEntity<Account> updateProfiles(@PathVariable String nickname, @RequestBody UpdateEntity updateEntity) {
        return ResponseEntity.ok(accountService.updateAccountByNickname(nickname, updateEntity));
    }

    @PutMapping("/profiles/{nickname}/password")
    public ResponseEntity<Account> updatePassword(@PathVariable String nickname, @Validated @RequestBody PasswordEntity passwordEntity, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(accountService.updatePassword(nickname, passwordEntity));
    }
}
