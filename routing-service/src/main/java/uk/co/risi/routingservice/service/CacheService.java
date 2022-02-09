package uk.co.risi.routingservice.service;

import uk.co.risi.routingservice.repository.ActivationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CacheService {

    private Logger logger = Logger.getLogger(getClass().getName());
    private final ActivationRepository repository;
    private Map<String, Integer> telematicsActivations = new ConcurrentHashMap<>();
    private Map<String, Integer> commandsActivation = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadCache() {
        repository.findAll().flatMap(activation -> {
            if (activation.getTelematicsActivations() > 0)
                telematicsActivations.put(activation.getVin(), activation.getTelematicsActivations());
            if (activation.getCommandsActivations() > 0)
                commandsActivation.put(activation.getVin(), activation.getCommandsActivations());
            return Mono.empty();
        }).subscribe();
    }

    public void addActivationForVin(String vin, boolean isTelematics) {
        if (isTelematics) {
            telematicsActivations.computeIfPresent(vin, (key, activation) -> ++activation);
            telematicsActivations.putIfAbsent(vin, 1);
            logger.info(vin + " telematics activations = " + telematicsActivations.get(vin));
        } else {
            commandsActivation.computeIfPresent(vin, (key, activation) -> ++activation);
            commandsActivation.putIfAbsent(vin, 1);
            logger.info(vin + " commands activations = " + commandsActivation.get(vin));
        }
    }

    public void removeActivationForVin(String vin, boolean isTelematics) {
        if (isTelematics) {
            if (telematicsActivations.containsKey(vin)) {
                if (telematicsActivations.get(vin) > 1)
                    telematicsActivations.computeIfPresent(vin, (key, activations) -> --activations);
                else
                    telematicsActivations.remove(vin);
                logger.info(vin + " telematics activations = " + telematicsActivations.get(vin));
            }
        } else {
            if (commandsActivation.containsKey(vin)) {
                if (commandsActivation.get(vin) > 1)
                    commandsActivation.computeIfPresent(vin, (key, activations) -> --activations);
                else
                    commandsActivation.remove(vin);
                logger.info(vin + " commands activations = " + commandsActivation.get(vin));
            }
        }
    }

    public boolean isVinRealtimeActivated(String vin, boolean isTelematics) {
        if (isTelematics)
            return telematicsActivations.containsKey(vin);
        else
            return commandsActivation.containsKey(vin);
    }
}
