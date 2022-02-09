package uk.co.risi.carmappingservice.repository;

import uk.co.risi.carmappingservice.domain.CarMapping;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CarMappingRepository extends ReactiveMongoRepository<CarMapping, String> {

    Mono<CarMapping> findByIp(String ip);
}
