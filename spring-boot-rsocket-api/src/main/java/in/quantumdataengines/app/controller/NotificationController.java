package in.quantumdataengines.app.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.security.Principal;
import java.time.Duration;

@Controller
public class NotificationController {

    @MessageMapping("notification.stream.{limit}")
    public Flux<Integer> notificationStream(@AuthenticationPrincipal String authentication, @DestinationVariable Integer limit) {
        System.out.println(authentication);
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(limit));
    }



}
