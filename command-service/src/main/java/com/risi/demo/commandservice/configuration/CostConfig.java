package com.risi.demo.commandservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("cost")
public class CostConfig {

    private double singleRequest;
    private double singleRequestFail;
    private double streamMessage;
}
