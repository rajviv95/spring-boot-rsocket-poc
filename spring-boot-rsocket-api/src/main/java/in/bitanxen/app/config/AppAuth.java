package in.bitanxen.app.config;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Decoder;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public class AppAuth implements Decoder {
    @Override
    public boolean canDecode(ResolvableType elementType, MimeType mimeType) {
        return false;
    }

    @Override
    public List<MimeType> getDecodableMimeTypes() {
        return null;
    }

    @Override
    public Mono decodeToMono(Publisher inputStream, ResolvableType elementType, MimeType mimeType, Map hints) {
        return null;
    }

    @Override
    public Flux decode(Publisher inputStream, ResolvableType elementType, MimeType mimeType, Map hints) {
        return null;
    }
}
