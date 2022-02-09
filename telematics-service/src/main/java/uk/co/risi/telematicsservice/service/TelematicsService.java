package uk.co.risi.telematicsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import uk.co.risi.telematicsservice.domain.CarData;
import uk.co.risi.telematicsservice.domain.TelematicsData;
import uk.co.risi.telematicsservice.domain.TelematicsIngestion;
import uk.co.risi.telematicsservice.repository.CarTelematicsRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscription;
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

    private final CarTelematicsRepository repository;
    @Autowired
    @Qualifier("LbWebClient")
    private WebClient.Builder lbWebClient;
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
        return repository.getLatestTelematicsData(vin, LocalDateTime.now().minusSeconds(5))
                .doOnNext(telematicsData -> {
                    if (telematicsData != null)
                        addBillToVin(vin, 0.03);
                    else
                        addBillToVin(vin, 0.01);
                });
    }

    public Flux<TelematicsData> generateStreamForVin(String vin) {
        var sub = new Subscription[1];
        var counter = new int[1];
        return Flux.interval(Duration.ofSeconds(1))
                .log()
                .doOnSubscribe(subscription -> sub[0] = subscription)
                .flatMap(aLong -> repository.getLatestTelematicsData(vin, LocalDateTime.now().minusSeconds(1))
                        .switchIfEmpty(Mono.fromRunnable(() -> {
                            ++counter[0];
                            if (counter[0] == 30)
                                sub[0].cancel();
                        })))
                .doOnNext(telematicsData -> {
                    if (telematicsData != null) {
                        counter[0] = 0;
                        addBillToVin(vin, 0.02);
                    }
                });
    }

    private void addBillToVin(String vin, double amount) {
        lbWebClient.build()
                .put()
                .uri("http://billing-service/" + vin + "/" + amount)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
