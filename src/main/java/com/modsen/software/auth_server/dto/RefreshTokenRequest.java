package com.modsen.software.auth_server.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(@NotNull String refreshToken) {
}
