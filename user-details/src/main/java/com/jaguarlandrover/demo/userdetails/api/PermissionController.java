package com.jaguarlandrover.demo.userdetails.api;

import com.jaguarlandrover.demo.userdetails.domain.Permission;
import com.jaguarlandrover.demo.userdetails.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;

    @PostMapping("${ep.user-service.permissions.create}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Permission> createPermission(@PathVariable String permission) {
        return service.createPermission(permission);
    }

    @GetMapping("${ep.user-service.permissions.all}")
    public Flux<Permission> retrievePermissions() {
        return service.retrievePermissions();
    }

    @DeleteMapping("${ep.user-service.permissions.remove}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Long> deletePermission(@PathVariable String permission){
        return service.removePermission(permission);
    }
}
