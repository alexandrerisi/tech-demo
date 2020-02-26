package com.jaguarlandrover.demo.telematicsservice.configuration.endpoints;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app")
public class AppNames {

    private String billing;
}
