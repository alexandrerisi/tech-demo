package com.jaguarlandrover.demo.carmappingservice.api;

import com.jaguarlandrover.demo.carmappingservice.domain.CommandMessage;
import com.jaguarlandrover.demo.carmappingservice.domain.TelematicsMessage;
import com.jaguarlandrover.demo.carmappingservice.service.CarMappingService;
import com.jaguarlandrover.demo.carmappingservice.domain.CarMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CarMappingController {

    private final CarMappingService service;

    @PostMapping("${ep.carmapping.telematics-ingest}")
    public void ingestTelematicsData(@RequestBody @Valid TelematicsMessage data) {
        service.publishDataForIp(data);
    }

    @PostMapping("${ep.carmapping.commands-ingest}")
    public void ingestCommand(@RequestBody CommandMessage command) {
        service.publishDataForIp(command);
    }

    @GetMapping("${ep.carmapping.mappings.all}")
    public Flux<CarMapping> retrieveAllMappings() {
        return service.retrieveAllIpMappings();
    }

    @PostMapping("${ep.carmapping.mappings.create}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CarMapping> createMapping(@RequestBody CarMapping mapping) {
        return service.createMapping(mapping);
    }

    @DeleteMapping("${ep.carmapping.mappings.remove}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteIpMapping(@PathVariable String ip) {
        return service.deleteMapping(ip);
    }

    @GetMapping("/test")
    public Mono<TelematicsMessage> test() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        var msg = new TelematicsMessage(1, 2, 3);
        msg.setIdentifier("Identifier");
        return Mono.just(msg);
    }
}
