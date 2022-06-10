package com.fm.demo.nacos.service.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yugj
 * @date 2022/6/6 下午1:38.
 */
@Slf4j
@RestController
@RequestMapping("/http/demo")
public class DemoController {

  /**
   * curl -X GET http://127.0.0.1:8889/http/demo/get
   * @return
   */
  @GetMapping("/get")
  public String get() {

    log.info("get request");
    return "{}";
  }

  @RequestMapping("/post")
  public String post(@RequestBody DemoDto data) {

    log.info("post request, name: {}, age :{}", data.getName(), data.getAge());
    return "abc";
  }

  @Data
  static class DemoDto {
    private String name;
    private Integer age;
  }

}
