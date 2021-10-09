package com.example.entity.account;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Account {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String nickname;

    @NotNull
    @JsonIgnore
    private String password;

    private String userId; // api

    private LocalDateTime createdAt;

    private String bio;
    private String url;
    private String occupation;
    private String location;

    @OneToMany(mappedBy = "account")
    private List<AccountTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<AccountZone> zones = new ArrayList<>();

    public void addTag(AccountTag tag) {
        if(!tags.contains(tag)){
            tags.add(tag);
            tag.setAccount(this);
        }
    }
    public void removeTag(AccountTag tag) {
        if(tags.contains(tag)) {
            tags.remove(tag);
            tag.setAccount(null);
        }
    }
    public void addZone(AccountZone zone) {
        if(!zones.contains(zone)) {
            zones.add(zone);
            zone.setAccount(this);
        }
    }
    public void removeZone(AccountZone zone) {
        if(zones.contains(zone)) {
            zones.remove(zone);
            zone.setAccount(null);
        }
    }
}
