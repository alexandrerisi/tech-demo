package com.risi.demo.commandservice.service;

import com.risi.demo.commandservice.configuration.CostConfig;
import com.risi.demo.commandservice.configuration.endpoints.BillingEndpoints;
import com.risi.demo.commandservice.domain.CarData;
import com.risi.demo.commandservice.domain.CommandData;
import com.risi.demo.commandservice.domain.CommandIngestion;
import com.risi.demo.commandservice.repository.CarCommandRepository;
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
public class CarCommandService {

    @Value("${commands.search-period}")
    private int searchPeriod;
    @Value("${stream.termination.attempts}")
    private int streamTerminationAttempts;
    private final CarCommandRepository repository;
    private final WebClient.Builder lbWebClient;
    private final BillingEndpoints billingEndpoints;
    private final CostConfig costConfig;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public Mono<UpdateResult> dataIngest(CommandIngestion data) {
        logger.info("Ingesting Command Data -> " + data);
        return repository.pushCommandData(data)
                .flatMap(updateResult -> {
                    if (updateResult.getMatchedCount() == 0)
                        return repository.save(new CarData(data.getVin()))
                                .flatMap(carData -> repository.pushCommandData(data));
                    return Mono.just(updateResult);
                });
    }

    public Flux<CarData> retrieveCommands() {
        return repository.findAll();
    }

    public Mono<CommandData> retrieveLatestCommandsForVin(String vin) {
        return repository.getLatestCommandData(vin, LocalDateTime.now().minusSeconds(5))
                .doOnNext(commandData -> {
                    if (commandData != null && !commandData.getCommand().isBlank())
                        addBillToVin(vin, costConfig.getSingleRequest());
                });
    }

    public Flux<CommandData> generateStreamForVin(String vin) {
        var sub = new Subscription[1];
        var counter = new int[1];
        return Flux.interval(Duration.ofSeconds(searchPeriod))
                .log()
                .doOnSubscribe(subscription -> sub[0] = subscription)
                .flatMap(aLong -> repository.getLatestCommandData(vin, LocalDateTime.now().minusSeconds(searchPeriod))
                        .switchIfEmpty(Mono.fromRunnable(() -> {
                            ++counter[0];
                            if (counter[0] == streamTerminationAttempts)
                                sub[0].cancel();
                        })))
                .doOnNext(telematicsData -> {
                    if (telematicsData != null) {
                        counter[0] = 0;
                        addBillToVin(vin, costConfig.getStreamMessage());
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
