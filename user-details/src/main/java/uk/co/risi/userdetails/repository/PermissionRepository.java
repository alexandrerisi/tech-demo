package uk.co.risi.userdetails.repository;

import uk.co.risi.userdetails.domain.Permission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PermissionRepository extends ReactiveCrudRepository<Permission, String> {
    Mono<Long> deleteByDescription(String permission);
}
