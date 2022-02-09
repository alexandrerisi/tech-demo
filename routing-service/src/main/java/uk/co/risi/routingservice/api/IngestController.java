package uk.co.risi.routingservice.api;

import uk.co.risi.routingservice.service.IngestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ingest")
@RequiredArgsConstructor
public class IngestController {

    private final IngestService service;

    @PostMapping("/telematics/{vin}")
    public Mono publishTelematics(@PathVariable String vin, @RequestBody Object data) {
        return service.publishToRealtime(vin, data, true);
    }

    @PostMapping("/commands/{vin}")
    public Mono publishCommands(@PathVariable String vin, @RequestBody Object data) {
        return service.publishToRealtime(vin, data, false);
    }
}
