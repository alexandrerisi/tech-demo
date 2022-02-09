package uk.co.risi.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import uk.co.risi.gateway.domain.JlrUser;
import uk.co.risi.gateway.domain.json.JlrUserJson;
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

    @Autowired
    @Qualifier("LbWebClient")
    private WebClient.Builder lbWebClient;
    private final JwtService jwtService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

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
        var serviceName = isTelematics ? "telematics" : "command";
        return lbWebClient.build().get()
                .uri("http://" + serviceName + "-service/"
                        + (isTelematics ? "telematics" : "commands") + "/stream/"
                        + vin)
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
        var serviceName = isTelematics ? "telematics" : "command";
        return lbWebClient.build().get()
                .uri("http://" + serviceName + "-service/" + (isTelematics ? "telematics" : "commands") + "/" + vin)
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
        var service = isTelematics ? "telematics/" : "commands/";
        logger.info(action + service + " for " + vin);
        lbWebClient.build()
                .method(isActivation ? HttpMethod.POST : HttpMethod.DELETE)
                .uri("http://routing-service/realtime/" + service + vin)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Flux retrieveAllBills() {
        logger.info("Retrieving bills...");
        return lbWebClient.build()
                .get()
                .uri("http://billing-service/bills")
                .retrieve()
                .bodyToFlux(Object.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono createUser(Object user) {
        logger.info("Requesting user creation...");
        return lbWebClient.build()
                .post()
                .uri("http://user-details/users")
                .body(Mono.just(user), Object.class)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Flux retrieveActivations() {
        logger.info("Retrieving realtime activations...");
        return lbWebClient.build()
                .get()
                .uri("http://routing-service/realtime/all")
                .retrieve()
                .bodyToFlux(Object.class);
    }

    public Mono<String> generateJwtToken(String username, String password) {
        logger.info("Requesting user -> " + username + "/" + password);
        return lbWebClient.build()
                .get()
                .uri("http://user-details/users/" + username + "/" + password)
                .retrieve()
                .bodyToMono(JlrUserJson.class)
                .map(userJson -> jwtService.generateToken(JlrUser.jsonToUser(userJson)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono addCarMapping(Object mapping) {
        logger.info("Adding Mapping to Vin/IP");
        return lbWebClient.build()
                .post().uri("http://carmapping-service/mappings")
                .body(Mono.just(mapping), Object.class)
                .retrieve()
                .bodyToMono(Object.class);
    }
}
