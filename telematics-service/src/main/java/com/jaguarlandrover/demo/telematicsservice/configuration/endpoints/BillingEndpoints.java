package com.jaguarlandrover.demo.telematicsservice.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("ep.billing")
public class BillingEndpoints {

    private final AppNames appNames;

    private String add;

    public void setAdd(String add) {
        this.add = appNames.getBilling() + add;
    }
}
