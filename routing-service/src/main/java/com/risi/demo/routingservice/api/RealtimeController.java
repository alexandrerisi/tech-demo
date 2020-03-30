package com.risi.demo.routingservice.api;

import com.risi.demo.routingservice.domain.Activation;
import com.risi.demo.routingservice.service.RealtimeActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RealtimeController {

    private final RealtimeActivationService realtimeActivationService;

    @PostMapping("${ep.routing.realtime-telematics-activate}")
    public Mono<Void> addTelematicsActivation(@PathVariable String vin) {
        return realtimeActivationService.addActivation(vin, true);
    }

    @DeleteMapping("${ep.routing.realtime-telematics-deactivate}")
    public Mono<Void> removeTelematicsActivation(@PathVariable String vin) {
        return realtimeActivationService.removeActivation(vin, true);
    }

    @GetMapping("${ep.routing.realtime-telematics-info}")
    public Mono retrieveTelematicsActivations(@PathVariable String vin) {
        return realtimeActivationService.retrieveActivationByVin(vin);
    }

    @PostMapping("${ep.routing.realtime-commands-activate}")
    public Mono<Void> addCommandsActivation(@PathVariable String vin) {
        return realtimeActivationService.addActivation(vin, false);
    }

    @DeleteMapping("${ep.routing.realtime-commands-deactivate}")
    public Mono<Void> removeCommandsActivation(@PathVariable String vin) {
        return realtimeActivationService.removeActivation(vin, false);
    }

    @GetMapping("${ep.routing.realtime-commands-info}")
    public Mono retrieveCommandsActivations(@PathVariable String vin) {
        return realtimeActivationService.retrieveActivationByVin(vin);
    }

    @GetMapping("${ep.routing.realtime-activations}")
    public Flux<Activation> getAllActivations() {
        return realtimeActivationService.retrieveAllActivations();
    }
}
