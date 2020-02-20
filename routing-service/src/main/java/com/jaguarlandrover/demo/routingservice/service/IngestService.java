package com.jaguarlandrover.demo.routingservice.service;

import com.jaguarlandrover.demo.routingservice.configuration.endpoints.CommandsEndpoints;
import com.jaguarlandrover.demo.routingservice.configuration.endpoints.TelematicsEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IngestService {
    //private Logger logger = Logger.getLogger(getClass().getName());

    private final WebClient.Builder lbWebClient;
    private final CacheService service;
    private final TelematicsEndpoints telematicsEndpoints;
    private final CommandsEndpoints commandsEndpoints;

    public Mono publishToRealtime(String vin, Object data, boolean isTelematics) {
        if (service.isVinRealtimeActivated(vin, isTelematics)) {
            var endpoint = isTelematics ? telematicsEndpoints.getIngest() : commandsEndpoints.getIngest();
            //logger.info("Publishing data to " + serviceName);
            return lbWebClient.build()
                    .post()
                    .uri(endpoint)
                    .body(Mono.just(data), Object.class)
                    .retrieve()
                    .bodyToMono(Void.class);
        }
        return Mono.empty();
    }
}
