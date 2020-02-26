package com.jaguarlandrover.demo.telematicsservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("cost")
public class CostConfiguration {

    private double singleRequest;
    private double singleRequestFail;
    private double streamMessage;
}
