package com.risi.demo.carmappingservice.config.endpoints;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("app")
public class AppNames {

    private String datalake;
    private String routing;
}
