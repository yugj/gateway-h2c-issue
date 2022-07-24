package com.fm.demo.nacos.service;

import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author yugj
 * @date 2022/6/6 上午11:44.
 */
@SpringBootApplication
public class NacosServiceStarter {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(NacosServiceStarter.class);
    application.run(args).start();
  }

  @Bean
  public ConfigurableServletWebServerFactory tomcatCustomizer() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

    Http2Protocol protocol = new Http2Protocol();
    protocol.setOverheadDataThreshold(0);
    factory.addConnectorCustomizers(connector -> connector.addUpgradeProtocol(protocol));
    return factory;
  }

}
