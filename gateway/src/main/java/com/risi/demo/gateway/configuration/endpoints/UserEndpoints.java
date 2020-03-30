package com.risi.demo.gateway.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ep.user-service")
@Getter
@RequiredArgsConstructor
public class UserEndpoints {

    private final AppNames appNames;

    private String login;
    private String all;
    private String create;

    public void setLogin(String login) {
        this.login = appNames.getUserdetails() + login;
    }

    public void setAll(String all) {
        this.all = appNames.getUserdetails() + all;
    }

    public void setCreate(String create) {
        this.create = appNames.getUserdetails() + create;
    }
}
