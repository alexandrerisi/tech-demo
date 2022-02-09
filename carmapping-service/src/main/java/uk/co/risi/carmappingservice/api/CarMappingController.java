package uk.co.risi.carmappingservice.api;

import uk.co.risi.carmappingservice.domain.CommandMessage;
import uk.co.risi.carmappingservice.domain.TelematicsMessage;
import uk.co.risi.carmappingservice.service.CarMappingService;
import uk.co.risi.carmappingservice.domain.CarMapping;
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

    @PostMapping("/telematics")
    public void ingestTelematicsData(@RequestBody @Valid TelematicsMessage data) {
        service.publishDataForIp(data);
    }

    @PostMapping("/commands")
    public void ingestCommand(@RequestBody CommandMessage command) {
        service.publishDataForIp(command);
    }

    @GetMapping("/mappings")
    public Flux<CarMapping> retrieveAllMappings() {
        return service.retrieveAllIpMappings();
    }

    @PostMapping("/mappings")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CarMapping> createMapping(@RequestBody CarMapping mapping) {
        return service.createMapping(mapping);
    }

    @DeleteMapping("/mappings/{ip}")
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
