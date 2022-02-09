package uk.co.risi.routingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class IngestService {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    @Qualifier("LbWebClient")
    private WebClient.Builder lbWebClient;
    private final CacheService service;


    public Mono publishToRealtime(String vin, Object data, boolean isTelematics) {
        if (service.isVinRealtimeActivated(vin, isTelematics)) {
            var serviceName = (isTelematics ? "telematics" : "command") + "-service";
            //logger.info("Publishing data to " + serviceName);
            return lbWebClient.build()
                    .post()
                    .uri("http://" + serviceName + "/ingest")
                    .body(Mono.just(data), Object.class)
                    .retrieve()
                    .bodyToMono(Void.class);
        }
        return Mono.empty();
    }
}
