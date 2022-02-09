package uk.co.risi.datalake.repository;

import uk.co.risi.datalake.domain.CommandData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CommandRepository extends ReactiveCrudRepository<CommandData, UUID> {
}
