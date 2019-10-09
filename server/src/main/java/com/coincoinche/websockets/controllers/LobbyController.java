package com.coincoinche.websockets.controllers;

import com.coincoinche.MatchMaker;
import com.coincoinche.websockets.messages.MessageType;
import com.coincoinche.websockets.messages.SocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyController {
  private static final Logger logger = LoggerFactory.getLogger(LobbyController.class);

  @Autowired private SimpMessagingTemplate template;

  private MatchMaker matchMaker;

  public LobbyController() {
    this.matchMaker = new MatchMaker(this);
  }

  /**
   * Sample websocket endpoint.
   *
   * @param message - incoming message.
   * @return message acknowledging reception.
   */
  @MessageMapping("/lobby/join")
  @SendTo("/topic/lobby")
  public SocketMessage joinLobby(@Payload SocketMessage message) {
    logger.debug(String.format("Received message %s", message.getType()));

    if (!message.getType().equals(MessageType.JOIN_LOBBY)) {
      return new SocketMessage(MessageType.INVALID_MESSAGE);
    }

    this.matchMaker.register("testUsername2");

    return new SocketMessage(MessageType.SUCCESS);
  }

  public void gameStart() {
    this.template.convertAndSend("/topic/lobby", new SocketMessage(MessageType.GAME_START));
  }
}
