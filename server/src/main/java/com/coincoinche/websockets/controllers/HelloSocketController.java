package com.coincoinche.websockets.controllers;

import com.coincoinche.websockets.messages.HelloSocketMessage;
import com.coincoinche.websockets.messages.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class HelloSocketController {
  private static final Logger logger = LoggerFactory.getLogger(HelloSocketController.class);

  /**
   * Sample websocket endpoint.
   *
   * @param message - incoming message.
   * @return message acknowledging reception.
   */
  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public HelloSocketMessage greeting(@Payload HelloSocketMessage message) {
    logger.debug(String.format("Received message %s: %s", message.getType(), message.getContent()));
    return new HelloSocketMessage(
        String.format("Server received message: %s", message.getContent()),
        "Server",
        MessageType.HELLO);
  }
}
