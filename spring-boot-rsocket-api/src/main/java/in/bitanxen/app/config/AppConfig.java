package in.bitanxen.app.config;

import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.invocation.reactive.ArgumentResolverConfigurer;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.metadata.BearerTokenAuthenticationEncoder;
import org.springframework.util.MimeType;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Configuration
public class AppConfig {

    /*
    @Bean
    public RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new BearerTokenAuthenticationEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                .metadataExtractorRegistry(registry -> {
                    registry.metadataToExtract(MimeType.valueOf("message/x.rsocket.authentication.v0"), AppAuth.class, "auth");
                })
                .build();
    }

     */

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies) {
        RSocketMessageHandler messageHandler = new RSocketMessageHandler();
        messageHandler.setRSocketStrategies(rSocketStrategies);
        ArgumentResolverConfigurer args = messageHandler
                .getArgumentResolverConfigurer();
        args.addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        return messageHandler;
    }

    @MessageExceptionHandler
    public Mono<ResponseEntity<String>> exception(final Exception exception)
    {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage()));
    }
}
