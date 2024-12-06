package com.modsen.software.auth_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DriverRequest {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private Long carId;
    private String status;
    private Boolean isDeleted;
}
