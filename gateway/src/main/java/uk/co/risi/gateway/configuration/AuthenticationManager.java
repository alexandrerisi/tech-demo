package uk.co.risi.gateway.configuration;

import uk.co.risi.gateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.concurrent.Callable;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService service;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.fromCallable(() -> {
                    var callable = new Callable<Authentication>() {
                        @Override
                        public Authentication call() {
                            var authToken = authentication.getPrincipal().toString();
                            String username = null;
                            try {
                                username = service.getUsernameFromToken(authToken);
                            } catch (Exception ignored) {
                            }
                            if (username != null && service.validateToken(authToken)) {
                                var claims = service.getAllClaimsFromToken(authToken);
                                var claimsValues = claims.values();
                                var authorities = new HashSet<GrantedAuthority>();
                                claimsValues.forEach(o -> authorities.add(new SimpleGrantedAuthority(o.toString())));
                                return new UsernamePasswordAuthenticationToken(username, null + " ", authorities);
                            } else
                                return null;
                        }
                    };
                    return callable.call();
                }
        );

        /* How to make a trans reactive.

        var authToken = authentication.getCredentials().toString();
        String username = null;
        try {
            username = service.getUsernameFromToken(authToken);
        } catch (Exception ignored) {
        }
        if (username != null && service.validateToken(authToken)) {
            var claims = service.getAllClaimsFromToken(authToken);
            var claimsValues = claims.values();
            var authorities = new HashSet<GrantedAuthority>();
            claimsValues.forEach(o -> authorities.add(new SimpleGrantedAuthority(o.toString())));
            var authUser = new UsernamePasswordAuthenticationToken(username, null + " ", authorities);
            return Mono.just(authUser);
        } else
            return Mono.empty();
        */
    }

}
