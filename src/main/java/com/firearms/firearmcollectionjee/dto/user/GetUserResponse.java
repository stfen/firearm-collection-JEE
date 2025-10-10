package com.firearms.firearmcollectionjee.dto.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * DTO returned when reading single user data (aligned with factory style example).
 */
public class GetUserResponse {
    private UUID id;
    private String login;
    private String email;
    private LocalDate birthDate;
    private List<String> roles = new ArrayList<>();

    public GetUserResponse() {}

    public GetUserResponse(UUID id, String login, String email, LocalDate birthDate, List<String> roles) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.birthDate = birthDate;
        if (roles != null) {
            this.roles = new ArrayList<>(roles);
        }
    }

    public UUID getId() { return id; }
    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public LocalDate getBirthDate() { return birthDate; }
    public List<String> getRoles() { return Collections.unmodifiableList(roles); }

    public void setId(UUID id) { this.id = id; }
    public void setLogin(String login) { this.login = login; }
    public void setEmail(String email) { this.email = email; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setRoles(List<String> roles) { this.roles = roles == null ? new ArrayList<>() : new ArrayList<>(roles); }
}
