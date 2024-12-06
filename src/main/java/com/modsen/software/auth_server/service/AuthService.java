package com.modsen.software.auth_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.auth_server.client.DriverServiceClient;
import com.modsen.software.auth_server.client.PassengerServiceClient;
import com.modsen.software.auth_server.configuration.KeycloakClientProperties;
import com.modsen.software.auth_server.dto.*;
import com.modsen.software.auth_server.exception.BaseCustomException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakClientProperties keycloakClientProperties;
    private final ObjectMapper mapper;
    private final Keycloak keycloakAdmin;
    private final PassengerServiceClient passengerServiceClient;
    private final DriverServiceClient driverServiceClient;

    public String auth(AuthRequest authRequest) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = "client_id=" + keycloakClientProperties.getClientId() +
                "&username=" + authRequest.username() +
                "&password=" + authRequest.password() +
                "&grant_type=" + keycloakClientProperties.getGrantType();

        var requestEntity = new HttpEntity<>(body, headers);
        var restTemplate = new RestTemplate();

        var response = restTemplate.exchange(
                keycloakClientProperties.getResourceUrl(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        if (response.getStatusCode().value() == 200) {
            return response.getBody();
        }

        throw new BaseCustomException(HttpStatus.UNAUTHORIZED.value(), response.getBody());
    }

    public String refresh(RefreshTokenRequest refreshTokenRequest) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = "client_id=" + keycloakClientProperties.getClientId() +
                "&refresh_token=" + refreshTokenRequest.refreshToken() +
                "&grant_type=refresh_token";

        var requestEntity = new HttpEntity<>(body, headers);
        var restTemplate = new RestTemplate();

        var response = restTemplate.exchange(
                keycloakClientProperties.getResourceUrl(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        if (response.getStatusCode().value() == 200) {
            return response.getBody();
        }

        throw new BaseCustomException(HttpStatus.UNAUTHORIZED.value(), response.getBody());
    }

    @SneakyThrows
    public ResponseEntity<String> signUpPassenger(PassengerRegistrationRequest passengerRegistrationRequest) {
        var responseSavePassenger = savePassengerOnService(passengerRegistrationRequest);

        if (responseSavePassenger.getStatusCode().isError()) {
            return new ResponseEntity<>(responseSavePassenger.getBody(), responseSavePassenger.getStatusCode());
        }

        var createdUserResponse = mapper.readValue(responseSavePassenger.getBody(), PassengerResponse.class);

        UsersResource usersResource = keycloakAdmin.realm("cab-aggregator").users();
        UserRepresentation kcUser = getUserRepresentation(
                createdUserResponse.getId(), passengerRegistrationRequest.password(), createdUserResponse.getFullName(),
                createdUserResponse.getEmail(), "passenger");

        Response response = usersResource.create(kcUser);
        return new ResponseEntity<>(response.getStatusInfo().getReasonPhrase(), HttpStatus.valueOf(response.getStatus()));
    }

    @SneakyThrows
    public ResponseEntity<String> signUpDriver(DriverRegistrationRequest driverRegistrationRequest) {
        var responseSavePassenger = saveDriverOnService(driverRegistrationRequest);

        if (responseSavePassenger.getStatusCode().isError()) {
            return new ResponseEntity<>(responseSavePassenger.getBody(), responseSavePassenger.getStatusCode());
        }

        var createdUserResponse = mapper.readValue(responseSavePassenger.getBody(), PassengerResponse.class);

        UsersResource usersResource = keycloakAdmin.realm("cab-aggregator").users();
        UserRepresentation kcUser = getUserRepresentation(
                createdUserResponse.getId(), driverRegistrationRequest.password(), createdUserResponse.getFullName(),
                createdUserResponse.getEmail(), "driver");

        Response response = usersResource.create(kcUser);
        return new ResponseEntity<>(response.getStatusInfo().getReasonPhrase(), HttpStatus.valueOf(response.getStatus()));
    }

    private static UserRepresentation getUserRepresentation(Long id, String password, String fullName, String email, String role) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(fullName);
        kcUser.setCredentials(List.of(passwordCredentials));
        kcUser.setEmail(email);
        kcUser.setRealmRoles(List.of(role));
        kcUser.setAttributes(Map.of("id", List.of(id.toString())));

        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        return kcUser;
    }

    private ResponseEntity<String> savePassengerOnService(PassengerRegistrationRequest passengerRegistrationRequest) {
        var passengerRequest = PassengerRequest.builder()
                .id(null)
                .phone(passengerRegistrationRequest.phone())
                .fullName(passengerRegistrationRequest.fullName())
                .email(passengerRegistrationRequest.email())
                .isDeleted(false)
                .build();

        return passengerServiceClient.savePassenger(passengerRequest);
    }

    private ResponseEntity<String> saveDriverOnService(DriverRegistrationRequest driverRegistrationRequest) {
        var driverRequest = DriverRequest.builder()
                .id(null)
                .phone(driverRegistrationRequest.phone())
                .fullName(driverRegistrationRequest.fullName())
                .email(driverRegistrationRequest.email())
                .gender(driverRegistrationRequest.gender())
                .isDeleted(false)
                .build();

        return driverServiceClient.saveDriver(driverRequest);
    }
}
