package com.risi.demo.gateway.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ep.commands")
@Getter
@RequiredArgsConstructor
public class CommandsEndpoints {

    private final AppNames appNames;

    private String ingest;
    private String stream;
    private String all;
    private String commands;

    public void setIngest(String ingest) {
        this.ingest = appNames.getCommand() + ingest;
    }

    public void setStream(String stream) {
        this.stream = appNames.getCommand() + stream;
    }

    public void setAll(String all) {
        this.all = appNames.getCommand() + all;
    }

    public void setCommands(String commands) {
        this.commands = appNames.getCommand() + commands;
    }
}
