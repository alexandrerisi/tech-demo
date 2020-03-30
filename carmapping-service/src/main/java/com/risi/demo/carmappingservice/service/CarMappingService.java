package com.risi.demo.carmappingservice.service;

import com.risi.demo.carmappingservice.config.endpoints.DatalakeEndpoints;
import com.risi.demo.carmappingservice.config.endpoints.RoutingEndpoints;
import com.risi.demo.carmappingservice.domain.CarMapping;
import com.risi.demo.carmappingservice.domain.CommandMessage;
import com.risi.demo.carmappingservice.domain.MessageIdentifier;
import com.risi.demo.carmappingservice.domain.TelematicsMessage;
import com.risi.demo.carmappingservice.repository.CarMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CarMappingService {

    private final CacheService cacheService;
    private final CarMappingRepository repository;
    private final WebClient.Builder lbWebClient;
    private final ReactiveCircuitBreakerFactory cbFactory;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private final DatalakeEndpoints datalakeEndpoints;
    private final RoutingEndpoints routingEndpoints;

    public Flux<CarMapping> retrieveAllIpMappings() {
        return repository.findAll();
    }

    public Mono<CarMapping> createMapping(CarMapping mapping) {
        logger.info("Adding mapping -> " + mapping);
        return repository.save(mapping);
    }

    public Mono<Void> deleteMapping(String ip) {
        return repository.deleteById(ip);
    }

    public void publishDataForIp(MessageIdentifier identifier) {
        var vinForIp = cacheService.getVinForIp(identifier.getIdentifier());
        if (vinForIp == null)
            return;
        logger.info("Setting Vin for => " + identifier);
        identifier.setIdentifier(vinForIp);
        publishData(identifier);
    }

    private void publishData(MessageIdentifier message) {
        var datalakeEndpoint = message instanceof TelematicsMessage ?
                datalakeEndpoints.getTelematicsIngest() : datalakeEndpoints.getCommandsIngest();
        var routingEndpoint = message instanceof TelematicsMessage ?
                routingEndpoints.getPublishTelematics() : routingEndpoints.getPublishCommands();
        List<String> uris = List.of(datalakeEndpoint, routingEndpoint.replace("{vin}", message.getIdentifier()));
        uris.forEach(uri -> lbWebClient.build()
                .post()
                .uri(uri)
                .body(
                        Mono.just(message),
                        message instanceof TelematicsMessage ? TelematicsMessage.class : CommandMessage.class
                )
                .retrieve()
                .bodyToMono(Void.class)
                //Circuit breaker
                .transform(it -> cbFactory.create("timeout").run(it, throwable -> {
                    // Do something fancy here!
                    logger.severe("Circuit opened for " + uri);
                    return Mono.empty();
                })).subscribe()
        );
    }
}
