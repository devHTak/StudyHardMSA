package com.GatewayService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public static class Config {}

    public final Environment environment;

    @Autowired
    public GlobalFilter(Environment environment) {
        super(Config.class);
        this.environment = environment;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("{}: {}", request.getId(), request.getPath().value());
            log.info("Local Port: {}, token.secret: {}, token.expiration_time: {}",
                    environment.getProperty("server.port"), environment.getProperty("token.secret"),environment.getProperty("token.expiration_time"));

            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("RESPONSE STATUS CODE: {}", response.getStatusCode());
            }));
        });
    }
}
