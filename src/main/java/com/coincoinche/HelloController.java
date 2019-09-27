package com.coincoinche;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller to test Spring Boot. TODO nockty: remove this code when no longer necessary.
 */
@RestController
public class HelloController {

  @RequestMapping("/greetings")
  public String greet() {
    return "Greetings from Spring Boot!";
  }
}
