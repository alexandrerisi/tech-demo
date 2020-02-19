package com.jaguarlandrover.demo.routingservice.api;

import com.jaguarlandrover.demo.routingservice.service.IngestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class IngestController {

    private final IngestService service;

    @PostMapping("${ep.routing.publish-telematics}")
    public Mono publishTelematics(@PathVariable String vin, @RequestBody Object data) {
        return service.publishToRealtime(vin, data, true);
    }

    @PostMapping("${ep.routing.publish-commands}")
    public Mono publishCommands(@PathVariable String vin, @RequestBody Object data) {
        return service.publishToRealtime(vin, data, false);
    }
}
