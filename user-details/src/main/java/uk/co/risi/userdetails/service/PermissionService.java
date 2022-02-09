package uk.co.risi.userdetails.service;

import uk.co.risi.userdetails.domain.Permission;
import uk.co.risi.userdetails.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository repository;

    public Mono<Permission> createPermission(String permission){
        return repository.save(new Permission(null, permission));
    }

    public Flux<Permission> retrievePermissions() {
        return repository.findAll();
    }

    public Mono<Long> removePermission(String permission) {
        return repository.deleteByDescription(permission);
    }
}
