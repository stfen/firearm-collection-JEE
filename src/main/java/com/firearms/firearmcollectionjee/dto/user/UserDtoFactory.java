package com.firearms.firearmcollectionjee.dto.user;

import com.firearms.firearmcollectionjee.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Factory / mapper class converting between User domain model and DTOs.
 * Keeps mapping logic in one place.
 */
public class UserDtoFactory {

    private UserDtoFactory() {
    }

    public static User toDomain(UserCreateRequestDto createDto) {
        if (createDto == null) {
            return null;
        }
        return User.builder()
                .id(UUID.randomUUID())
                .login(createDto.getLogin())
                .email(createDto.getEmail())
                .birthDate(createDto.getBirthDate())
                .roles(new ArrayList<>(createDto.getRoles()))
                .build();
    }

    public static User toDomain(UserUpdateRequestDto updateDto, User existing) {
        if (updateDto == null) {
            return existing;
        }
        // Preserve ID and only mutate allowed fields
        return User.builder()
                .id(existing.getId())
                .login(updateDto.getLogin() != null ? updateDto.getLogin() : existing.getLogin())
                .email(updateDto.getEmail() != null ? updateDto.getEmail() : existing.getEmail())
                .birthDate(updateDto.getBirthDate() != null ? updateDto.getBirthDate() : existing.getBirthDate())
                .roles(updateDto.getRoles() != null ? new ArrayList<>(updateDto.getRoles()) : existing.getRoles())
                .build();
    }

    public static UserResponseDto toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDto(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthDate(),
                user.getRoles() == null ? List.of() : new ArrayList<>(user.getRoles())
        );
    }

    public static List<UserResponseDto> toResponseList(List<User> users) {
        if (users == null) {
            return List.of();
        }
        return users.stream()
                .filter(Objects::nonNull)
                .map(UserDtoFactory::toResponse)
                .toList();
    }
}
