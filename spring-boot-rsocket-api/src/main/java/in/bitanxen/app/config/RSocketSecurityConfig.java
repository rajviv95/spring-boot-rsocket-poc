package in.bitanxen.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.PayloadInterceptorOrder;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.authentication.AuthenticationPayloadInterceptor;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
public class RSocketSecurityConfig {

    private final TokenReactiveAuthenticationManager tokenReactiveAuthenticationManager;

    public RSocketSecurityConfig(TokenReactiveAuthenticationManager tokenReactiveAuthenticationManager) {
        this.tokenReactiveAuthenticationManager = tokenReactiveAuthenticationManager;
    }

    @Bean
    public PayloadSocketAcceptorInterceptor acceptorInterceptor(RSocketSecurity rsocket, AuthenticationPayloadInterceptor payloadInterceptor) {
        rsocket
                .authorizePayload(authz ->
                        authz.setup().hasRole("SETUP")
                        .anyRequest().authenticated()
                        .anyExchange().permitAll()
                )
                .addPayloadInterceptor(payloadInterceptor);
        return rsocket.build();
    }

    @Bean
    public AuthenticationPayloadInterceptor authenticationPayloadInterceptor(TokenAuthenticationConverter tokenAuthenticationConverter) {
        //TokenReactiveAuthenticationManager manager = new TokenReactiveAuthenticationManager();
        AuthenticationPayloadInterceptor result = new AuthenticationPayloadInterceptor(tokenReactiveAuthenticationManager);
        result.setAuthenticationConverter(tokenAuthenticationConverter);
        result.setOrder(PayloadInterceptorOrder.JWT_AUTHENTICATION.getOrder());
        return result;
    }
}