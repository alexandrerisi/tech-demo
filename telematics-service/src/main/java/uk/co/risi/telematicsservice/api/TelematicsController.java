package uk.co.risi.telematicsservice.api;

import uk.co.risi.telematicsservice.domain.CarData;
import uk.co.risi.telematicsservice.domain.TelematicsData;
import uk.co.risi.telematicsservice.domain.TelematicsIngestion;
import uk.co.risi.telematicsservice.service.TelematicsService;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TelematicsController {

    private final TelematicsService telematicsService;

    @PostMapping("/ingest")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UpdateResult> ingest(@RequestBody TelematicsIngestion json) {
        return telematicsService.dataIngestion(json);
    }

    @GetMapping(value = "/vins")
    public Flux<CarData> getVins() {
        return telematicsService.retrieveTelematics();
    }

    @GetMapping("/telematics/{vin}")
    public Mono<TelematicsData> retrieveLatestTelematicsForVin(@PathVariable String vin) {
        return telematicsService.retrieveLatestTelematicsForVin(vin);
    }

    @GetMapping(value = "/telematics/stream/{vin}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TelematicsData> streamForVin(@PathVariable String vin) {
        return telematicsService.generateStreamForVin(vin);
    }
}
