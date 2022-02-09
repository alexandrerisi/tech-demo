package uk.co.risi.commandservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import uk.co.risi.commandservice.domain.CarData;
import uk.co.risi.commandservice.domain.CommandData;
import uk.co.risi.commandservice.domain.CommandIngestion;
import uk.co.risi.commandservice.repository.CarCommandRepository;
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
public class CarCommandService {

    private final CarCommandRepository repository;

    @Qualifier("LbWebClient")
    @Autowired
    private WebClient.Builder lbWebClient;
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
                        addBillToVin(vin, 0.03);
                });
    }

    //todo add comments Mila.
    public Flux<CommandData> generateStreamForVin(String vin) {
        var sub = new Subscription[1];
        var counter = new int[1];
        return Flux.interval(Duration.ofSeconds(1))
                .log()
                .doOnSubscribe(subscription -> sub[0] = subscription)
                .flatMap(aLong -> repository.getLatestCommandData(vin, LocalDateTime.now().minusSeconds(1))
                        .switchIfEmpty(Mono.fromRunnable(() -> {
                            ++counter[0];
                            if (counter[0] == 100)
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
                .bodyToMono(Void.class).subscribe();
    }
}
