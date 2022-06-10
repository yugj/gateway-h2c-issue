package com.fm.api.gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * filter route auto configuration
 *
 * @author yugj
 * @date 2022/6/6 上午10:17.
 */
@Configuration
public class FilterAutoConfiguration {


  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

    return builder.routes()
            .route("api", r -> r.path("/**")
            .uri("http://localhost:8889"))
            .build();
  }

}
