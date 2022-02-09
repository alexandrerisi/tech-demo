package uk.co.risi.datalake.service;

import uk.co.risi.datalake.domain.CommandData;
import uk.co.risi.datalake.domain.TelematicsData;
import uk.co.risi.datalake.repository.CommandRepository;
import uk.co.risi.datalake.repository.TelematicsRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class DataLakeService {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private final TelematicsRepository telematicsRepository;
    private final CommandRepository commandRepository;
    //private UnicastProcessor<TelematicsData> hotSource = UnicastProcessor.create();
    private Sinks.Many<TelematicsData> hotSource = Sinks.many().unicast().onBackpressureBuffer();

    @PostConstruct
    public void postConstruct() {
        Flux<TelematicsData> hotFlux = hotSource.asFlux().publish().autoConnect()
                .buffer(Duration.ofSeconds(5))
                .flatMap(telematicsDataList -> telematicsRepository.saveAll(reducedSet(telematicsDataList)));
        hotFlux.doOnSubscribe(subscription -> logger.info("Starting HotFlux"))
                .doOnTerminate(() -> logger.info("Ending HotFlux")).subscribe();
    }

    public void ingestTelematics(TelematicsData data) {
        hotSource.tryEmitNext(data);
    }

    public Flux<TelematicsData> retrieveAllTelematics() {
        return telematicsRepository.findAll().sort();
    }

    public Mono<CommandData> ingestCommand(CommandData data) {
        return commandRepository.save(data);
    }

    public Flux<CommandData> retrieveAllCommands() {
        return commandRepository.findAll();
    }

    private Collection<TelematicsData> reducedSet(List<TelematicsData> list) {
        var reducedMap = new HashMap<String, TelematicsData>();
        list.forEach(telematicsData -> {
            if (reducedMap.containsKey(telematicsData.getVin())
                    && reducedMap.get(telematicsData.getVin()).getTimestamp().isAfter(telematicsData.getTimestamp()))
                return;
            reducedMap.put(telematicsData.getVin(), telematicsData);
        });
        return reducedMap.values();
    }
}
