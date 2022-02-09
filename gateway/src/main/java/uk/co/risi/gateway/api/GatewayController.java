package uk.co.risi.gateway.api;

import uk.co.risi.gateway.service.GatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    @GetMapping("/login/{username}/{password}")
    public Mono<String> login(@PathVariable String username, @PathVariable String password) {
        return gatewayService.generateJwtToken(username, password);
    }

    @GetMapping("/test")
    @PreAuthorize("hasAnyAuthority('MPH', 'KPH')")
    public Mono<String> test() {
        return Mono.just("Works");
    }

    @GetMapping(value = "/telematics/stream/{vin}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux streamTelematics(@PathVariable String vin) {
        return gatewayService.generateTelematicsStream(vin);
    }

    @GetMapping(value = "/commands/stream/{vin}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux streamCommands(@PathVariable String vin) {
        return gatewayService.generateCommandsStream(vin);
    }

    @GetMapping("/telematics/{vin}")
    public Mono latestTelematicsForVin(@PathVariable String vin) {
        return gatewayService.retrieveLatestTelematicsForVin(vin);
    }

    @GetMapping("/commands/{vin}")
    public Mono latestCommandsForVin(@PathVariable String vin) {
        return gatewayService.retrieveLatestCommandsForVin(vin);
    }

    @GetMapping("/bills")
    public Flux retrieveAllBills() {
        return gatewayService.retrieveAllBills();
    }

    @PostMapping("/users")
    public Mono createUser(@RequestBody Object user) {
        return gatewayService.createUser(user);
    }

    @GetMapping("/activations")
    public Flux retrieveActivations() {
        return gatewayService.retrieveActivations();
    }

    @PostMapping("/mappings")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono addCarMapping(@RequestBody Object mapping) {
        return gatewayService.addCarMapping(mapping);
    }
}
