package uk.co.risi.userdetails;

import brave.sampler.Sampler;
import uk.co.risi.userdetails.domain.Permission;
import uk.co.risi.userdetails.domain.User;
import uk.co.risi.userdetails.repository.PermissionRepository;
import uk.co.risi.userdetails.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class UserDetailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDetailsApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Bean
    public CommandLineRunner runner(UserService service, PermissionRepository repository) {
        return args -> {
            // Permission for everything.
            var permissions = Set.of(
                    new Permission(null, "commands"),
                    new Permission(null, "telematics"),
                    new Permission(null, "mph"),
                    new Permission(null, "1G6DW677X60160257"),
                    new Permission(null, "VINFORSIMULATORRX"),
                    new Permission(null, "admin"));
            repository.saveAll(permissions).blockLast();
            service.createUser(new User(null, "jlrUserAll", "123", permissions)).subscribe();

            // Permission for nothing.
            service.createUser(new User(null, "jlrUserNone", "123", new ArrayList<>())).subscribe();

            // Has Vins but no permissions.
            service.createUser(new User(null, "jlrUserHasVin", "123",
                    List.of(new Permission(null, "1G6DW677X60160257"),
                            new Permission(null, "VINFORSIMULATORRX")))).subscribe();
        };
    }

    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
