package com.quantumdataengines.app.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.rsocket.core.DefaultConnectionSetupPayload;
import io.rsocket.metadata.CompositeMetadata;
import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.messaging.rsocket.MetadataExtractor;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.rsocket.api.PayloadExchange;
import org.springframework.security.rsocket.authentication.PayloadExchangeAuthenticationConverter;
import org.springframework.security.rsocket.metadata.BearerTokenMetadata;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenAuthenticationConverter implements PayloadExchangeAuthenticationConverter {

    private final MetadataExtractor metadataExtractor;

    public TokenAuthenticationConverter(RSocketMessageHandler rSocketMessageHandler) {
        this.metadataExtractor = rSocketMessageHandler.getMetadataExtractor();
    }


    /*
    @Override
    public Mono<Authentication> convert(PayloadExchange exchange) {
        Map<String, Object> data = this.metadataExtractor.extract(exchange.getPayload(), exchange.getMetadataMimeType());
        System.out.println("data : "+data);
        ByteBuf metadata = exchange.getPayload().metadata();
        System.out.println("TokenAuthenticationConverter 1 : "+exchange.getMetadataMimeType()+" : "+metadata.toString(StandardCharsets.UTF_8));
        CompositeMetadata compositeMetadata = new CompositeMetadata(metadata, false);
        System.out.println("TokenAuthenticationConverter 2 :"+compositeMetadata.stream().map(entry -> entry.getContent().toString(StandardCharsets.UTF_8)).collect(Collectors.toList()));
        for (CompositeMetadata.Entry entry : compositeMetadata) {
            System.out.println("TokenAuthenticationConverter entry "+entry+" "+entry.getMimeType()+" "+entry.getContent().toString(StandardCharsets.UTF_8));
            if (WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.toString().equals(entry.getMimeType())) {
                ByteBuf content = entry.getContent();
                String token = content.toString(StandardCharsets.UTF_8);
                System.out.println("Auth Token"+ token);
                return Mono.just(new UsernamePasswordAuthenticationToken(null, token, null));
            }
        }
        return Mono.empty();
    }
     */

    @Override
    public Mono<Authentication> convert(PayloadExchange exchange) {
        Map<String, Object> data = this.metadataExtractor.extract(exchange.getPayload(), exchange.getMetadataMimeType());
        System.out.println(data+" "+exchange.getPayload().getMetadataUtf8());
        ArrayList<GrantedAuthority> objects = new ArrayList<>();
        objects.add(new SimpleGrantedAuthority("ROLE_SETUP"));

        return Mono.justOrEmpty(data.get(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.toString()))
                .cast(String.class).log().map(s -> new UsernamePasswordAuthenticationToken(s, s, objects));
    }
}
