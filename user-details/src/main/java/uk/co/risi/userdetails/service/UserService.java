package uk.co.risi.userdetails.service;

import uk.co.risi.userdetails.domain.User;
import uk.co.risi.userdetails.repository.PermissionRepository;
import uk.co.risi.userdetails.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public Mono<User> retrieveUser(User user) {
        return userRepository.findByUsername(user.getUsername()).flatMap(dbUser -> {
            if (encoder.matches(user.getPassword(), dbUser.getPassword()))
                return Mono.just(dbUser);
            return Mono.empty();
        });
    }

    public Mono<User> createUser(User user) {

        var userToSave = new User(null, user.getUsername(), encoder.encode(user.getPassword()), new HashSet<>());
        return userRepository.findByUsername(user.getUsername())
                .switchIfEmpty(
                        userRepository.save(userToSave)
                                .map(dbUser -> {
                                    permissionRepository.findAll()
                                            .filter(permission -> user.getPermissions().contains(permission))
                                            .flatMap(permission ->
                                                    userRepository.addPermission(dbUser.getUsername(), permission))
                                            .subscribe();
                                    return dbUser;
                                })
                );
    }

    public Flux<User> retrieveUsers() {
        return userRepository.findAll();
    }
}
