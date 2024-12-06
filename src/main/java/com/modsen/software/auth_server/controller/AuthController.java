package com.modsen.software.auth_server.controller;

import com.modsen.software.auth_server.dto.AuthRequest;
import com.modsen.software.auth_server.dto.DriverRegistrationRequest;
import com.modsen.software.auth_server.dto.PassengerRegistrationRequest;
import com.modsen.software.auth_server.dto.RefreshTokenRequest;
import com.modsen.software.auth_server.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public String auth(@RequestBody @Valid AuthRequest authRequest) {
        return authService.auth(authRequest);
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public String refresh(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return authService.refresh(refreshTokenRequest);
    }

    @PostMapping("/passenger/signup")
    public ResponseEntity<String> signUpPassenger(@RequestBody @Valid PassengerRegistrationRequest passengerRegistrationRequest) {
        return authService.signUpPassenger(passengerRegistrationRequest);
    }
    
    @PostMapping("/driver/signup")
    public ResponseEntity<String> signUpDriver(@RequestBody @Valid DriverRegistrationRequest driverRegistrationRequest) {
        return authService.signUpDriver(driverRegistrationRequest);
    }
}
