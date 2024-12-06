package com.modsen.software.auth_server.configuration;

import com.modsen.software.auth_server.exception.BaseCustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new ResponseStatusException(HttpStatus.valueOf(response.status()), readResponseBody(response));
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }

    @SneakyThrows
    private String readResponseBody(Response response) {
        if (response.body() == null) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}

