package com.risi.demo.routingservice.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ep.commands")
@Getter
@RequiredArgsConstructor
public class CommandsEndpoints {

    private final AppNames appNames;

    private String ingest;

    public void setIngest(String ingest) {
        this.ingest = appNames.getCommand() + ingest;
    }
}
