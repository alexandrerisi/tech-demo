package com.risi.demo.gateway.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ep.telematics")
@Setter
@Getter
@RequiredArgsConstructor
public class TelematicsEndpoints {

    private final AppNames appNames;

    private String ingest;
    private String stream;
    private String all;
    private String telematics;

    public void setIngest(String ingest) {
        this.ingest = appNames.getTelematics() + ingest;
    }

    public void setStream(String stream) {
        this.stream = appNames.getTelematics() + stream;
    }

    public void setAll(String all) {
        this.all = appNames.getTelematics() + all;
    }

    public void setTelematics(String telematics) {
        this.telematics = appNames.getTelematics() + telematics;
    }
}
