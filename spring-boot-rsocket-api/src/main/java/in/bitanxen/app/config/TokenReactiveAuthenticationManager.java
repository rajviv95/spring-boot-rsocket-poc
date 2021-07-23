package in.bitanxen.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
//@Component
public class TokenReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    public TokenReactiveAuthenticationManager() {
        System.out.println("TokenReactiveAuthenticationManager");
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .log("TokenReactiveAuthenticationManager")
                .map(a -> {
                    System.out.println("auth "+a.getPrincipal() +" "+ a.getCredentials());
                    return a;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Missing Authentication Token")));
    }
}
