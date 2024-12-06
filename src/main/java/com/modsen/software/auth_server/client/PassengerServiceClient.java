package com.modsen.software.auth_server.client;

import com.modsen.software.auth_server.dto.PassengerRequest;
import com.modsen.software.auth_server.exception.ServiceNotAvailable;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "passenger-service")
public interface PassengerServiceClient {

    @PostMapping("/api/v1/passengers")
    @CircuitBreaker(name = "passengerServiceCircuitBreaker", fallbackMethod = "handleSavePassengerFallback")
    ResponseEntity<String> savePassenger(@RequestBody PassengerRequest passengerRequest);

    default ResponseEntity<String> handleSavePassengerFallback(PassengerRequest passengerRequest, Throwable ex) {
        throw new ServiceNotAvailable("Passenger service not available");
    }
}
