package com.risi.demo.carmappingservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("constant")
public class CounterProperties {

    private int carbindingCounter;
}
