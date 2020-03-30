package com.risi.demo.carmappingservice.repository;

import com.risi.demo.carmappingservice.domain.CarMapping;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CarMappingRepository extends ReactiveMongoRepository<CarMapping, String> {

    Mono<CarMapping> findByIp(String ip);
}
