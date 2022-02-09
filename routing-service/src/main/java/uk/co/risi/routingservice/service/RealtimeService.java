package uk.co.risi.routingservice.service;

import uk.co.risi.routingservice.domain.Activation;
import uk.co.risi.routingservice.repository.ActivationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RealtimeService {

    private final CacheService cacheService;
    private final ActivationRepository repository;

    public Mono<Void> addActivation(String vin, boolean isTelematics) {
        return repository.updateActivations(vin, isTelematics, true)
                .flatMap(updateResult -> {
                    if (updateResult.getMatchedCount() == 0)
                        return repository.save(new Activation(vin, 0, 0))
                                .flatMap(activation -> repository.updateActivations(vin, isTelematics, true)
                                        .map(updateRlt -> {
                                            cacheService.addActivationForVin(vin, isTelematics);
                                            return updateRlt;
                                        }));
                    cacheService.addActivationForVin(vin, isTelematics);
                    return Mono.just(updateResult);
                }).then();
    }

    public Mono<Void> removeActivation(String vin, boolean isTelematics) {
        return repository.updateActivations(vin, isTelematics, false)
                .map(updateResult -> {
                    cacheService.removeActivationForVin(vin, isTelematics);
                    return updateResult;
                }).then();
    }

    public Mono<Activation> retrieveActivationByVin(String vin) {
        return repository.findById(vin);
    }

    public Flux<Activation> retrieveAllActivations() {
        return repository.findAll();
    }
}
