package com.jaguarlandrover.demo.gateway.configuration.endpoints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ep.billing")
@Getter
@RequiredArgsConstructor
public class BillingEndpoints {

    private final AppNames appNames;

    private String add;
    private String bills;

    public void setAdd(String add) {
        this.add = appNames.getBilling() + add;
    }

    public void setBills(String bills) {
        this.bills = appNames.getBilling() + bills;
    }
}
