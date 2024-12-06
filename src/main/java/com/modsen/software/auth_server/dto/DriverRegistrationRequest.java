package com.modsen.software.auth_server.dto;

import jakarta.validation.constraints.NotNull;

public record DriverRegistrationRequest(
        @NotNull String fullName,
        @NotNull String email,
        @NotNull String password,
        @NotNull String phone,
        @NotNull String gender
) {
}
