package com.jaguarlandrover.demo.datalake.api;

import com.jaguarlandrover.demo.datalake.domain.CommandData;
import com.jaguarlandrover.demo.datalake.domain.TelematicsData;
import com.jaguarlandrover.demo.datalake.service.DataLakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class DataLakeController {

    private final DataLakeService service;

    @PostMapping("/telematics")
    @ResponseStatus(HttpStatus.CREATED)
    public void ingestTelematics(@RequestBody TelematicsData data) {
        service.ingestTelematics(data);
    }

    @PostMapping("/commands")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CommandData> ingestTelematics(@RequestBody CommandData data) {
        return service.ingestCommand(data);
    }

    @GetMapping("/telematics")
    public Flux<TelematicsData> retrieveAllTelematics() {
        return service.retrieveAllTelematics();
    }

    @GetMapping("/commands")
    public Flux<CommandData> retrieveAllCommands() {
        return service.retrieveAllCommands();
    }
}
