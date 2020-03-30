package com.risi.demo.datalake.repository;

import com.risi.demo.datalake.domain.CommandData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CommandRepository extends ReactiveCrudRepository<CommandData, UUID> {
}
