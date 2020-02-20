package com.jaguarlandrover.demo.gateway.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ep.routing")
@Getter
@RequiredArgsConstructor
public class RoutingEndpoints {

    private final AppNames appNames;

    private String publishTelematics;
    private String publishCommands;
    private String realtimeTelematicsActivate;
    private String realtimeTelematicsDeactivate;
    private String realtimeTelematicsInfo;
    private String realtimeCommandsActivate;
    private String realtimeCommandsDeactivate;
    private String realtimeCommandsInfo;
    private String realtimeActivations;

    public void setPublishTelematics(String publishTelematics) {
        this.publishTelematics = appNames.getRouting() + publishTelematics;
    }

    public void setPublishCommands(String publishCommands) {
        this.publishCommands = appNames.getRouting() + publishCommands;
    }

    public void setRealtimeTelematicsActivate(String realtimeTelematicsActivate) {
        this.realtimeTelematicsActivate = appNames.getRouting() + realtimeTelematicsActivate;
    }

    public void setRealtimeTelematicsDeactivate(String realtimeTelematicsDeactivate) {
        this.realtimeTelematicsDeactivate = appNames.getRouting() + realtimeTelematicsDeactivate;
    }

    public void setRealtimeTelematicsInfo(String realtimeTelematicsInfo) {
        this.realtimeTelematicsInfo = appNames.getRouting() + realtimeTelematicsInfo;
    }

    public void setRealtimeCommandsActivate(String realtimeCommandsActivate) {
        this.realtimeCommandsActivate = appNames.getRouting() + realtimeCommandsActivate;
    }

    public void setRealtimeCommandsDeactivate(String realtimeCommandsDeactivate) {
        this.realtimeCommandsDeactivate = appNames.getRouting() + realtimeCommandsDeactivate;
    }

    public void setRealtimeCommandsInfo(String realtimeCommandsInfo) {
        this.realtimeCommandsInfo = appNames.getRouting() + realtimeCommandsInfo;
    }

    public void setRealtimeActivations(String realtimeActivations) {
        this.realtimeActivations = appNames.getRouting() + realtimeActivations;
    }
}
