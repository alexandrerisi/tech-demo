package com.jaguarlandrover.demo.datalake.service;

import com.jaguarlandrover.demo.datalake.domain.CommandData;
import com.jaguarlandrover.demo.datalake.domain.TelematicsData;
import com.jaguarlandrover.demo.datalake.repository.CommandRepository;
import com.jaguarlandrover.demo.datalake.repository.TelematicsRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

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
    private UnicastProcessor<TelematicsData> hotSource = UnicastProcessor.create();

    @PostConstruct
    public void postConstruct() {
        Flux<TelematicsData> hotFlux = hotSource.publish().autoConnect()
                .buffer(Duration.ofSeconds(5))
                .flatMap(telematicsDataList -> telematicsRepository.saveAll(reducedSet(telematicsDataList)));
        hotFlux.doOnSubscribe(subscription -> logger.info("Starting HotFlux"))
                .doOnTerminate(() -> logger.info("Ending HotFlux")).subscribe();
    }

    public void ingestTelematics(TelematicsData data) {
        hotSource.onNext(data);
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
