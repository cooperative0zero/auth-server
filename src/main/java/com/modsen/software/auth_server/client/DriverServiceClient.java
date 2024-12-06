package com.modsen.software.auth_server.client;

import com.modsen.software.auth_server.dto.DriverRequest;
import com.modsen.software.auth_server.exception.ServiceNotAvailable;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "driver-service")
public interface DriverServiceClient {

    @PostMapping("/api/v1/drivers")
    @CircuitBreaker(name = "driverServiceCircuitBreaker", fallbackMethod = "handleSaveDriverFallback")
    ResponseEntity<String> saveDriver(@RequestBody DriverRequest driverRequest);

    default ResponseEntity<String> handleSaveDriverFallback(DriverRequest driverRequest, Throwable ex) {
        throw new ServiceNotAvailable("Driver service not available");
    }
}
