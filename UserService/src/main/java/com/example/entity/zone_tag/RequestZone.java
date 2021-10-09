package com.example.entity.zone_tag;

import com.example.entity.zone_tag.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RequestZone {

    @NotNull
    private Address address;
}
