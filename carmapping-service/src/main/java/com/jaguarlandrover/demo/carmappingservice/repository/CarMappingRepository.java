package com.jaguarlandrover.demo.carmappingservice.repository;

import com.jaguarlandrover.demo.carmappingservice.domain.CarMapping;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CarMappingRepository extends ReactiveMongoRepository<CarMapping, String> {

    Mono<CarMapping> findByIp(String ip);
}
