package com.risi.demo.telematicsservice.service;

import com.risi.demo.telematicsservice.configuration.CostConfiguration;
import com.risi.demo.telematicsservice.configuration.endpoints.BillingEndpoints;
import com.risi.demo.telematicsservice.domain.CarData;
import com.risi.demo.telematicsservice.domain.TelematicsData;
import com.risi.demo.telematicsservice.domain.TelematicsIngestion;
import com.risi.demo.telematicsservice.repository.CarTelematicsRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TelematicsService {

    @Value("${stream.termination.attempts}")
    private int streamTerminationAttempts;
    @Value("${telematics.search-period}")
    private int telematicsSearchPeriod;
    private final CarTelematicsRepository repository;
    private final WebClient.Builder lbWebClient;
    private final BillingEndpoints billingEndpoints;
    private final CostConfiguration costConfiguration;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public Mono<UpdateResult> dataIngestion(TelematicsIngestion json) {
        logger.info("Ingesting telematics data -> " + json);
        return repository.pushTelematicsData(json)
                .flatMap(updateResult -> {
                    if (updateResult.getMatchedCount() == 0)
                        return repository.save(new CarData(json.getVin()))
                                .flatMap(carData -> repository.pushTelematicsData(json));
                    return Mono.just(updateResult);
                });
    }

    public Flux<CarData> retrieveTelematics() {
        return repository.findAll();
    }

    public Mono<TelematicsData> retrieveLatestTelematicsForVin(String vin) {
        return repository.getLatestTelematicsData(vin, LocalDateTime.now().minusSeconds(telematicsSearchPeriod))
                .doOnNext(telematicsData -> {
                    if (telematicsData != null)
                        addBillToVin(vin, costConfiguration.getSingleRequest());
                    else
                        addBillToVin(vin, costConfiguration.getSingleRequestFail());
                });
    }

    //todo add comments Mila.
    public Flux<TelematicsData> generateStreamForVin(String vin) {
        var sub = new Subscription[1];
        var counter = new int[1];
        return Flux.interval(Duration.ofSeconds(5))
                .log()
                .doOnSubscribe(subscription -> sub[0] = subscription)
                .flatMap(aLong -> repository.getLatestTelematicsData(vin, LocalDateTime.now().minusSeconds(5))
                        .switchIfEmpty(Mono.fromRunnable(() -> {
                            ++counter[0];
                            if (counter[0] == streamTerminationAttempts)
                                sub[0].cancel();
                        })))
                .doOnNext(telematicsData -> {
                    if (telematicsData != null) {
                        counter[0] = 0;
                        addBillToVin(vin, costConfiguration.getStreamMessage());
                    }
                });
    }

    private void addBillToVin(String vin, double amount) {
        lbWebClient.build()
                .put()
                .uri(billingEndpoints.getAdd(), vin, amount)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
