package uk.co.risi.userdetails.api;

import uk.co.risi.userdetails.api.json.UserJson;
import uk.co.risi.userdetails.domain.User;
import uk.co.risi.userdetails.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping("/{username}/{password}")
    public Mono<UserJson> retrieveUser(@PathVariable String username, @PathVariable String password) {
        var user = new User(null, username, password, null);
        return service.retrieveUser(user).map(UserJson::userToJson);
    }

    @GetMapping("/all")
    public Flux<User> retrieveUsers() {
        return service.retrieveUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@RequestBody User user){
        return service.createUser(user);
    }
}
