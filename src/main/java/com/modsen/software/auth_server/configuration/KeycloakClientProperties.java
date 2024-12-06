package com.modsen.software.auth_server.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Getter
@Setter
public class KeycloakClientProperties {

    private String clientId;
    private String resourceUrl;
    private String grantType;
}
