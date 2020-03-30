package com.risi.demo.carmappingservice.config.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("ep.datalake")
@RequiredArgsConstructor
public class DatalakeEndpoints {

    private final AppNames appNames;

    private String telematicsIngest;
    private String commandsIngest;

    public void setTelematicsIngest(String telematicsIngest) {
        this.telematicsIngest = appNames.getDatalake() + telematicsIngest;
    }

    public void setCommandsIngest(String commandsIngest) {
        this.commandsIngest = appNames.getDatalake() + commandsIngest;
    }
}
