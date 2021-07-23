package in.bitanxen.app.config;

import io.rsocket.core.DefaultConnectionSetupPayload;
import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.rsocket.MetadataExtractor;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.rsocket.api.PayloadExchange;
import org.springframework.security.rsocket.authentication.PayloadExchangeAuthenticationConverter;
import org.springframework.security.rsocket.metadata.BearerTokenMetadata;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

public class TokenAuthenticationConverter implements PayloadExchangeAuthenticationConverter {

    private final MetadataExtractor metadataExtractor;

    public TokenAuthenticationConverter(RSocketMessageHandler rSocketMessageHandler) {
        System.out.println("TokenAuthenticationConverter");
        this.metadataExtractor = rSocketMessageHandler.getMetadataExtractor();
    }

    @Override
    public Mono<Authentication> convert(PayloadExchange payloadExchange) {
        return Mono.just(payloadExchange)
                .log()
                .map(pe -> {
                    Map<String, Object> extract = metadataExtractor.extract(payloadExchange.getPayload(), payloadExchange.getMetadataMimeType());
                    System.out.println(extract);
                    return extract;
                })
                .log()
                .map(m -> {
                    Object o = m.get(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
                    System.out.println(o);
                    return o;
                })
                .log()
                .cast(String.class)
                .map(s-> {
                    System.out.println("message/x.rsocket.authentication.v0 "+s);
                    return new UsernamePasswordAuthenticationToken(null, null, null);
                });
    }
}
