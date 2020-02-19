package com.jaguarlandrover.demo.gateway.configuration.endpoints;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppNames {

    private String billing;
    private String carmapping;
    private String command;
    private String datalake;
    private String routing;
    private String telematics;
    private String userdetails;
}
