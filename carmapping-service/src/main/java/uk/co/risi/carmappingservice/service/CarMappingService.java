package uk.co.risi.carmappingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import uk.co.risi.carmappingservice.domain.CarMapping;
import uk.co.risi.carmappingservice.domain.CommandMessage;
import uk.co.risi.carmappingservice.domain.MessageIdentifier;
import uk.co.risi.carmappingservice.domain.TelematicsMessage;
import uk.co.risi.carmappingservice.repository.CarMappingRepository;
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
    @Autowired
    @Qualifier("LbWebClient")
    private WebClient.Builder lbWebClient;
    private final ReactiveCircuitBreakerFactory cbFactory;
    private Logger logger = Logger.getLogger(this.getClass().getName());

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
        var serviceName = message instanceof TelematicsMessage ? "telematics" : "commands";
        List<String> uris = List.of(
                "http://data-lake/" + serviceName,
                "http://routing-service/ingest/" + serviceName + "/" + message.getIdentifier());
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
