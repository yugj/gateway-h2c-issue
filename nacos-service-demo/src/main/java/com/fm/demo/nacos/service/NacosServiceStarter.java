package com.fm.demo.nacos.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

}
