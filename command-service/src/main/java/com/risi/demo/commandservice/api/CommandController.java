package com.risi.demo.commandservice.api;

import com.risi.demo.commandservice.domain.CarData;
import com.risi.demo.commandservice.domain.CommandData;
import com.risi.demo.commandservice.domain.CommandIngestion;
import com.risi.demo.commandservice.service.CarCommandService;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CommandController {

    private final CarCommandService service;

    @PostMapping("/ingest")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UpdateResult> ingest(@RequestBody CommandIngestion data) {
        return service.dataIngest(data);
    }

    @GetMapping(value = "/vins", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<CarData> getAllCommands() {
        return service.retrieveCommands();
    }

    @GetMapping("/commands/{vin}")
    public Mono<CommandData> retrieveLatestTelematicsForVin(@PathVariable String vin) {
        return service.retrieveLatestCommandsForVin(vin);
    }

    @GetMapping(value = "/commands/stream/{vin}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CommandData> streamForVin(@PathVariable String vin) {
        return service.generateStreamForVin(vin);
    }
}
