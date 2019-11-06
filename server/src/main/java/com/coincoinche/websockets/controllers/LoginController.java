package com.coincoinche.websockets.controllers;

import com.coincoinche.events.Event;
import com.coincoinche.events.EventType;
import com.coincoinche.events.UserLoggedIn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class LoginController {
  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  /**
   * Endpoint called to log the user.
   *
   * @param message - incoming message.
   * @return message acknowledging reception.
   */
  @MessageMapping("/login")
  @SendTo("/topic/login")
  public Event logUser(@Payload UserLoggedIn message) {
    logger.debug(
        String.format(
            "Received message %s, username %s", message.getType(), message.getUsername()));

    if (!message.getType().equals(EventType.USER_LOGGED_IN)) {
      return new Event(EventType.INVALID_MESSAGE);
    }
    return new Event(EventType.SUCCESS);
  }
}