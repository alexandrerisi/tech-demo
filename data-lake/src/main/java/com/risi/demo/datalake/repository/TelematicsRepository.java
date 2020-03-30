package com.risi.demo.datalake.repository;

import com.risi.demo.datalake.domain.TelematicsData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TelematicsRepository extends ReactiveCrudRepository<TelematicsData, UUID> {
}
