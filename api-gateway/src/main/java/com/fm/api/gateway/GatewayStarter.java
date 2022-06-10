package com.fm.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author yugj
 * @date 2022/6/6 上午10:15.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayStarter {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(GatewayStarter.class);
    application.run(args).start();
  }

}
