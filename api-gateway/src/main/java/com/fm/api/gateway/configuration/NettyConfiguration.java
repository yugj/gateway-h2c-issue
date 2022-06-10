package com.fm.api.gateway.configuration;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.embedded.NettyWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import reactor.netty.http.HttpProtocol;

/**
 * @author yugj
 * @date 2022/6/8 上午10:29.
 */
@Configuration
public class NettyConfiguration {

    // https://projectreactor.io/docs/netty/release/reference/index.html#_http2
    @Bean
    public NettyWebServerFactoryCustomizer h2cServerCustomizer(Environment environment, ServerProperties serverProperties) {
        return new NettyWebServerFactoryCustomizer(environment, serverProperties) {
            @Override
            public void customize(NettyReactiveWebServerFactory factory) {
                factory.addServerCustomizers(httpServer -> httpServer.protocol(HttpProtocol.HTTP11, HttpProtocol.H2C));
                super.customize(factory);
            }
        };
    }


    @Bean
    public HttpClientCustomizer h2ClientCustomizer() {
        return httpClient -> httpClient.protocol(HttpProtocol.HTTP11, HttpProtocol.H2C);
    }
}
