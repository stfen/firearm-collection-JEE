package com.firearms.firearmcollectionjee.dto.user;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetUserResponse {
    private UUID id;
    private String login;
    private String name;
    private String email;
}

