package com.firearms.firearmcollectionjee.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class User implements Serializable {
    private UUID id;
    private String login;
    private LocalDate birthDate;
    private String email;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String avatarPath;
    private List<String> roles;
    private List<Firearm> firearms;

}

