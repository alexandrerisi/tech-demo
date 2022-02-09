package uk.co.risi.routingservice.api;

import uk.co.risi.routingservice.domain.Activation;
import uk.co.risi.routingservice.service.RealtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/realtime")
public class RealtimeController {

    private final RealtimeService realtimeService;

    @PostMapping("/telematics/{vin}")
    public Mono<Void> addTelematicsActivation(@PathVariable String vin) {
        return realtimeService.addActivation(vin, true);
    }

    @DeleteMapping("/telematics/{vin}")
    public Mono<Void> removeTelematicsActivation(@PathVariable String vin) {
        return realtimeService.removeActivation(vin, true);
    }

    @GetMapping("/telematics/{vin}")
    public Mono retrieveTelematicsActivations(@PathVariable String vin) {
        return realtimeService.retrieveActivationByVin(vin);
    }

    @PostMapping("/commands/{vin}")
    public Mono<Void> addCommandsActivation(@PathVariable String vin) {
        return realtimeService.addActivation(vin, false);
    }

    @DeleteMapping("/commands/{vin}")
    public Mono<Void> removeCommandsActivation(@PathVariable String vin) {
        return realtimeService.removeActivation(vin, false);
    }

    @GetMapping("/commands/{vin}")
    public Mono retrieveCommandsActivations(@PathVariable String vin) {
        return realtimeService.retrieveActivationByVin(vin);
    }

    @GetMapping("/all")
    public Flux<Activation> getAllActivations() {
        return realtimeService.retrieveAllActivations();
    }
}
