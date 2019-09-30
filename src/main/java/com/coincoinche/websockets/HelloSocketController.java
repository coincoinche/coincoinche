package com.coincoinche.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class HelloSocketController {
  private static final Logger logger = LoggerFactory.getLogger(HelloSocketController.class);

  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public SocketMessage greeting(@Payload SocketMessage message) throws Exception {
    logger.debug(String.format("Received message: %s", message.getContent()));
    return new SocketMessage(
        String.format("Server received message: %s", message.getContent()),
        "Server",
        MessageType.HELLO.getType());
  }
}
