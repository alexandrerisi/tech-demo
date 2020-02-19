package com.jaguarlandrover.demo.gateway.service;

import com.jaguarlandrover.demo.gateway.configuration.endpoints.*;
import com.jaguarlandrover.demo.gateway.domain.JlrUser;
import com.jaguarlandrover.demo.gateway.domain.json.JlrUserJson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class GatewayService {

    private final WebClient.Builder lbWebClient;
    private final JwtService jwtService;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private final TelematicsEndpoints telematicsEndpoints;
    private final CommandsEndpoints commandsEndpoints;
    private final UserEndpoints userEndpoints;
    private final RoutingEndpoints routingEndpoints;
    private final BillingEndpoints billingEndpoints;
    private final CarmappingEndpoints carmappingEndpoints;

    @PreAuthorize("hasAuthority('TELEMATICS')")
    public Flux generateTelematicsStream(String vin) {
        logger.info("Generating Telematics Stream for - " + vin);
        return generateStream(vin, true);
    }

    @PreAuthorize("hasAuthority('COMMANDS')")
    public Flux generateCommandsStream(String vin) {
        logger.info("Generating Command Stream for - " + vin);
        return generateStream(vin, false);
    }

    private Flux generateStream(String vin, boolean isTelematics) {
        var endpoint = isTelematics ? telematicsEndpoints.getStream() : commandsEndpoints.getStream();
        return lbWebClient.build().get()
                .uri(endpoint, vin)
                .retrieve()
                .bodyToFlux(Object.class)
                .doOnSubscribe(subscription -> realtimeTrigger(vin, isTelematics, true))
                .doOnCancel(() -> realtimeTrigger(vin, isTelematics, false))
                .doOnError(throwable -> realtimeTrigger(vin, isTelematics, false));
    }

    @PreAuthorize("hasAuthority('TELEMATICS')")
    public Mono retrieveLatestTelematicsForVin(String vin) {
        return retrieveLatestDataForVin(vin, true);
    }

    @PreAuthorize("hasAuthority('COMMANDS')")
    public Mono retrieveLatestCommandsForVin(String vin) {
        return retrieveLatestDataForVin(vin, false);
    }

    private Mono retrieveLatestDataForVin(String vin, boolean isTelematics) {
        var endpoint = isTelematics ? telematicsEndpoints.getTelematics() : commandsEndpoints.getCommands();
        return lbWebClient.build().get()
                .uri(endpoint, vin)
                .retrieve()
                .bodyToMono(Object.class)
                .doOnSubscribe(subscription -> realtimeTrigger(vin, isTelematics, true))
                .doOnTerminate(() -> Mono.delay(Duration.ofSeconds(30)).map(aLong -> {
                    realtimeTrigger(vin, isTelematics, false);
                    return aLong;
                }).subscribe());
    }

    private void realtimeTrigger(String vin, boolean isTelematics, boolean isActivation) {
        var action = isActivation ? "Adding subscription for " : "Subtracting subscription for ";
        var endpoint = isTelematics ? routingEndpoints.getRealtimeTelematicsActivate()
                : routingEndpoints.getRealtimeCommandsActivate();
        logger.info(action + vin + " " + endpoint);
        lbWebClient.build()
                .method(isActivation ? HttpMethod.POST : HttpMethod.DELETE)
                .uri(endpoint, vin)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Flux retrieveAllBills() {
        logger.info("Retrieving bills...");
        return lbWebClient.build()
                .get()
                .uri(billingEndpoints.getBills())
                .retrieve()
                .bodyToFlux(Object.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono createUser(Object user) {
        logger.info("Requesting user creation...");
        return lbWebClient.build()
                .post()
                .uri(userEndpoints.getCreate())
                .body(Mono.just(user), Object.class)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Flux retrieveActivations() {
        logger.info("Retrieving realtime activations...");
        return lbWebClient.build()
                .get()
                .uri(routingEndpoints.getRealtimeActivations())
                .retrieve()
                .bodyToFlux(Object.class);
    }

    public Mono<String> generateJwtToken(String username, String password) {
        logger.info("Requesting user -> " + username + "/" + password);
        return lbWebClient.build()
                .get()
                .uri(userEndpoints.getLogin(), username, password)
                .retrieve()
                .bodyToMono(JlrUserJson.class)
                .map(userJson -> jwtService.generateToken(JlrUser.jsonToUser(userJson)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono addCarMapping(Object mapping) {
        logger.info("Adding Mapping to Vin/IP");
        return lbWebClient.build()
                .post().uri(carmappingEndpoints.getMappingsCreate())
                .body(Mono.just(mapping), Object.class)
                .retrieve()
                .bodyToMono(Object.class);
    }
}
