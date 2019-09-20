package com.coincoinche;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Simple controller to test Spring Boot. */
@RestController
public class HelloController             {

  @RequestMapping("/greetings")
  public String greet() {
    return "Greetings from Spring Boot!";
  }
}
