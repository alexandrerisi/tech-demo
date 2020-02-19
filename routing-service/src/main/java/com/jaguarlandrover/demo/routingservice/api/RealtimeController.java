package com.jaguarlandrover.demo.routingservice.api;

import com.jaguarlandrover.demo.routingservice.domain.Activation;
import com.jaguarlandrover.demo.routingservice.service.RealtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RealtimeController {

    private final RealtimeService realtimeService;

    @PostMapping("${ep.routing.realtime-telematics-activate}")
    public Mono<Void> addTelematicsActivation(@PathVariable String vin) {
        return realtimeService.addActivation(vin, true);
    }

    @DeleteMapping("${ep.routing.realtime-telematics-deactivate}")
    public Mono<Void> removeTelematicsActivation(@PathVariable String vin) {
        return realtimeService.removeActivation(vin, true);
    }

    @GetMapping("${ep.routing.realtime-telematics-info}")
    public Mono retrieveTelematicsActivations(@PathVariable String vin) {
        return realtimeService.retrieveActivationByVin(vin);
    }

    @PostMapping("${ep.routing.realtime-commands-activate}")
    public Mono<Void> addCommandsActivation(@PathVariable String vin) {
        return realtimeService.addActivation(vin, false);
    }

    @DeleteMapping("${ep.routing.realtime-commands-deactivate}")
    public Mono<Void> removeCommandsActivation(@PathVariable String vin) {
        return realtimeService.removeActivation(vin, false);
    }

    @GetMapping("${ep.routing.realtime-commands-info}")
    public Mono retrieveCommandsActivations(@PathVariable String vin) {
        return realtimeService.retrieveActivationByVin(vin);
    }

    @GetMapping("${ep.routing.realtime-activations}")
    public Flux<Activation> getAllActivations() {
        return realtimeService.retrieveAllActivations();
    }
}
