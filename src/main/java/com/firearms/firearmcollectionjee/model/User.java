package com.firearms.firearmcollectionjee.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
    private UUID id;
    private String login;
    private LocalDate birthDate;
    private String email;
    private String avatarPath;
    private List<String> roles;

    public User() {}

    public User(UUID id, String login, LocalDate birthDate, String email, String avatarPath, List<String> roles) {
        this.id = id;
        this.login = login;
        this.birthDate = birthDate;
        this.email = email;
        this.avatarPath = avatarPath;
        this.roles = roles;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String login;
        private LocalDate birthDate;
        private String email;
        private String avatarPath;
        private List<String> roles;
        public Builder id(UUID id) { this.id = id; return this; }
        public Builder login(String login) { this.login = login; return this; }
        public Builder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder avatarPath(String avatarPath) { this.avatarPath = avatarPath; return this; }
        public Builder roles(List<String> roles) { this.roles = roles; return this; }
        public User build() { return new User(id, login, birthDate, email, avatarPath, roles); }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", avatarPath='" + (avatarPath != null ? "***" : null) + '\'' +
                ", roles=" + roles +
                '}';
    }
}
