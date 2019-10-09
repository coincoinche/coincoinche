package com.coincoinche.websockets.controllers;

import com.coincoinche.MatchMaker;
import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.CoincheGameRound;
import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.coincoinche.websockets.messages.MessageType;
import com.coincoinche.websockets.messages.SocketMessage;
import java.util.List;
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

    this.matchMaker.register("testUsername");

    return new SocketMessage(MessageType.SUCCESS);
  }

  public void gameStart(String[] usernames) {
    if (usernames.length != 4) {
      throw new IllegalArgumentException("The game must start with exactly 4 players");
    }

    Player player1 = new Player(usernames[0]);
    Player player2 = new Player(usernames[1]);
    Player player3 = new Player(usernames[2]);
    Player player4 = new Player(usernames[3]);

    Team redTeam = new Team(player1, player2, Team.Color.RED);
    Team blueTeam = new Team(player3, player4, Team.Color.BLUE);

    CoincheGame game = new CoincheGame(redTeam, blueTeam);
    CoincheGameRound currentRound = game.getCurrentRound();

    List<Card> cardsList1 = player1.getCards();
    List<Card> cardsList2 = player1.getCards();
    List<Card> cardsList3 = player1.getCards();
    List<Card> cardsList4 = player1.getCards();

    System.out.println("Player 1");
    for (Card card : cardsList1) {
      System.out.print(card + ", ");
    }
    System.out.println("Player 2");
    for (Card card : cardsList2) {
      System.out.print(card + ", ");
    }
    System.out.println("Player 3");
    for (Card card : cardsList3) {
      System.out.print(card + ", ");
    }
    System.out.println("Player 4");
    for (Card card : cardsList4) {
      System.out.print(card + ", ");
    }

    this.template.convertAndSend("/topic/lobby", new SocketMessage(MessageType.GAME_START));
  }
}
