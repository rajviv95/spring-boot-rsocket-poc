package in.quantumdataengines.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TokenReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    public TokenReactiveAuthenticationManager() {
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(a -> {
                    System.out.println("TokenReactiveAuthenticationManager "+a.getPrincipal() +" "+ a.getCredentials());
                    return a;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Missing Authentication Token")));
    }
}
