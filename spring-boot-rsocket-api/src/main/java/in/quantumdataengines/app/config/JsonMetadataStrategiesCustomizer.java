package in.quantumdataengines.app.config;

import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.security.rsocket.metadata.BearerTokenMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.util.Map;

@Component
public class JsonMetadataStrategiesCustomizer implements RSocketStrategiesCustomizer {

    public static final MimeType METADATA_MIME_TYPE = MimeType.valueOf("application/vnd.spring.rsocket.metadata+json");
    public static final MimeType METADATA_AUTH_MIME_TYPE = MimeType.valueOf("message/x.rsocket.authentication.v0");

    private static final ParameterizedTypeReference<Map<String,String>> METADATA_TYPE =
            new ParameterizedTypeReference<Map<String,String>>() {};

    @Override
    public void customize(RSocketStrategies.Builder strategies) {
        strategies.metadataExtractorRegistry(registry -> {
            //registry.metadataToExtract(METADATA_AUTH_MIME_TYPE, AuthTokenMetadata.class, "token");
            registry.metadataToExtract(METADATA_MIME_TYPE, METADATA_TYPE, (in, map) -> {
                map.putAll(in);
            });
        });
    }

}