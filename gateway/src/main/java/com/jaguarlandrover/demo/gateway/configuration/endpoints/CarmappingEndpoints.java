package com.jaguarlandrover.demo.gateway.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ep.carmapping")
@Getter
@RequiredArgsConstructor
public class CarmappingEndpoints {

    private final AppNames appNames;

    private String telematicsIngest;
    private String commandsIngest;
    private String mappingsCreate;
    private String mappingsRemove;
    private String mappingsAll;

    public void setTelematicsIngest(String telematicsIngest) {
        this.telematicsIngest = appNames.getCarmapping() + telematicsIngest;
    }

    public void setCommandsIngest(String commandsIngest) {
        this.commandsIngest = appNames.getCarmapping() + commandsIngest;
    }

    public void setMappingsCreate(String mappingsCreate) {
        this.mappingsCreate = appNames.getCarmapping() + mappingsCreate;
    }

    public void setMappingsRemove(String mappingsRemove) {
        this.mappingsRemove = appNames.getCarmapping() + mappingsRemove;
    }

    public void setMappingsAll(String mappingsAll) {
        this.mappingsAll = appNames.getCarmapping() + mappingsAll;
    }
}
