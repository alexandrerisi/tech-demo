package com.jaguarlandrover.demo.datalake.repository;

import com.jaguarlandrover.demo.datalake.domain.CommandData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CommandRepository extends ReactiveCrudRepository<CommandData, UUID> {
}
