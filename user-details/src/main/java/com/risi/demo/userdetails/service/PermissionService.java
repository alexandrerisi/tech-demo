package com.risi.demo.userdetails.service;

import com.risi.demo.userdetails.domain.Permission;
import com.risi.demo.userdetails.repository.PermissionRepository;
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
