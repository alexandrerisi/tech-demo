package com.jaguarlandrover.demo.datalake.repository;

import com.jaguarlandrover.demo.datalake.domain.TelematicsData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TelematicsRepository extends ReactiveCrudRepository<TelematicsData, UUID> {
}
