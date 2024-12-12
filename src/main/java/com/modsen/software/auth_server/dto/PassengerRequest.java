package com.modsen.software.auth_server.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PassengerRequest {

    @Min(1)
    private Long id;

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\d{6,}$")
    @NotBlank
    private String phone;

    @NotNull
    private Boolean isDeleted;

    private Float rating;
}

