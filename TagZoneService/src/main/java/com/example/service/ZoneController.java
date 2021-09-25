package com.example.service;

import com.example.entity.Address;
import com.example.entity.Zone;
import com.example.entity.ZoneValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag-zone-service/zones")
public class ZoneController {

    private final ZoneService zoneService;
    private final ZoneValidator zoneValiator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(zoneValiator);
    }

    @PostMapping
    public ResponseEntity<Zone> saveZone(@Validated @RequestBody Address address, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.saveZone(address));
    }

    @GetMapping
    public ResponseEntity<Boolean> existsZoneByAddress(@Validated @ModelAttribute Address address, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(zoneService.existsByAddress(address));
    }
}
