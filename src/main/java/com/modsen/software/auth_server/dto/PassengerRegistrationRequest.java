package com.modsen.software.auth_server.dto;

import jakarta.validation.constraints.NotNull;

public record PassengerRegistrationRequest(
        @NotNull String fullName,
        @NotNull String email,
        @NotNull String phone,
        @NotNull String password) {
}
