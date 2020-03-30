package com.risi.demo.carmappingservice.config.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("ep.routing")
@RequiredArgsConstructor
public class RoutingEndpoints {

    private final AppNames appNames;

    private String publishTelematics;
    private String publishCommands;

    public void setPublishTelematics(String publishTelematics) {
        this.publishTelematics = appNames.getRouting() + publishTelematics;
    }

    public void setPublishCommands(String publishCommands) {
        this.publishCommands = appNames.getRouting() + publishCommands;
    }
}
