package uk.co.risi.datalake.repository;

import uk.co.risi.datalake.domain.TelematicsData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface TelematicsRepository extends ReactiveCrudRepository<TelematicsData, UUID> {
}
