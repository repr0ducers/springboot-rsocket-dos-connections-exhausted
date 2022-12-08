package example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class Service {

    @MessageMapping("response")
    public Mono<Response> responseOverflow(Request request) {
        Response r = new Response();
        r.setMessage(request.getMessage());
        return Mono.just(r);
    }
}
