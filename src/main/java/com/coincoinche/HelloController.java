package com.coincoinche;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller to test Spring Boot. TODO nockty: remove this code when no longer necessary.
 */
@RestController
public class HelloController {

  private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

  /** Test endpoint for greetings. */
  @RequestMapping("/greetings")
  public String greet() {
    logger.trace("this is a TRACE log");
    logger.debug("this is a DEBUG log");
    logger.info("this is a INFO log");
    logger.warn("this is a WARN log");
    logger.error("this is a ERROR log");
    return "Greetings from Spring Boot!";
  }
}
