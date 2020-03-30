package com.risi.demo.userdetails.api;

import com.risi.demo.userdetails.api.json.UserJson;
import com.risi.demo.userdetails.domain.User;
import com.risi.demo.userdetails.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("${ep.user-service.login}")
    public Mono<UserJson> retrieveUser(@PathVariable String username, @PathVariable String password) {
        var user = new User(null, username, password, null);
        return service.retrieveUser(user).map(UserJson::userToJson);
    }

    @GetMapping("${ep.user-service.all}")
    public Flux<User> retrieveUsers() {
        return service.retrieveUsers();
    }

    @PostMapping("${ep.user-service.create}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@RequestBody User user){
        return service.createUser(user);
    }
}
