package com.jaguarlandrover.demo.gateway.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ep.user-service.permissions")
@Setter
@Getter
@RequiredArgsConstructor
public class UserPermissionsEndpoint {

    private final AppNames appNames;

    private String create;
    private String all;
    private String remove;

    public void setCreate(String create) {
        this.create = appNames.getUserdetails() + create;
    }

    public void setAll(String all) {
        this.all = appNames.getUserdetails() + all;
    }

    public void setRemove(String remove) {
        this.remove = appNames.getUserdetails() + remove;
    }
}
