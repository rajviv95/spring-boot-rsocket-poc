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

    private final RSocketMessageHandler rSocketMessageHandler;
    private final TokenReactiveAuthenticationManager tokenReactiveAuthenticationManager;

    public RSocketSecurityConfig(@Lazy RSocketMessageHandler rSocketMessageHandler, TokenReactiveAuthenticationManager tokenReactiveAuthenticationManager) {
        this.rSocketMessageHandler = rSocketMessageHandler;
        this.tokenReactiveAuthenticationManager = tokenReactiveAuthenticationManager;
    }

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
        RSocketMessageHandler rSocketMessageHandler = new RSocketMessageHandler();
        rSocketMessageHandler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        rSocketMessageHandler.setRSocketStrategies(strategies);
        return rSocketMessageHandler;
    }

    @Bean
    public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket, AuthenticationPayloadInterceptor payloadInterceptor) {
        rsocket
                .authorizePayload(authorize ->
                        authorize
                                .setup().permitAll()
                                .route("notification*").permitAll()
                                .anyRequest().authenticated()
                )
                .addPayloadInterceptor(payloadInterceptor);
        return rsocket.build();
    }

    @Bean
    AuthenticationPayloadInterceptor jwt() {
        //TokenReactiveAuthenticationManager manager = new TokenReactiveAuthenticationManager();
        AuthenticationPayloadInterceptor result = new AuthenticationPayloadInterceptor(tokenReactiveAuthenticationManager);
        result.setAuthenticationConverter(new TokenAuthenticationConverter(rSocketMessageHandler));
        result.setOrder(PayloadInterceptorOrder.JWT_AUTHENTICATION.getOrder());
        return result;
    }
}