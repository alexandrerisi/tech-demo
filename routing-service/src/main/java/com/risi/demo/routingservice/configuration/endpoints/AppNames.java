package com.risi.demo.routingservice.configuration.endpoints;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppNames {

    private String telematics;
    private String command;
}
