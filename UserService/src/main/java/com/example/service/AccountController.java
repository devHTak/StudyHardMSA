package com.example.service;

import com.example.entity.Account;
import com.example.entity.PasswordEntity;
import com.example.entity.SignUpEntity;
import com.example.entity.UpdateEntity;
import com.example.entity.validate.SignUpValidator;
import lombok.RequiredArgsConstructor;
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
