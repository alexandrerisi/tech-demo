package uk.co.risi.userdetails.api;

import uk.co.risi.userdetails.domain.Permission;
import uk.co.risi.userdetails.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService service;

    @PostMapping("/{permission}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Permission> createPermission(@PathVariable String permission) {
        return service.createPermission(permission);
    }

    @GetMapping("/all")
    public Flux<Permission> retrievePermissions() {
        return service.retrievePermissions();
    }

    @DeleteMapping("/{permission}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Long> deletePermission(@PathVariable String permission){
        return service.removePermission(permission);
    }
}
