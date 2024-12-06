package com.modsen.software.auth_server.exception;

import org.springframework.http.HttpStatus;

public class ServiceNotAvailable extends BaseCustomException {
    public ServiceNotAvailable(String customMessage) {
        super(HttpStatus.SERVICE_UNAVAILABLE.value(), customMessage);
    }
}
