package com.coincoinche.websockets.controllers;

import com.coincoinche.MatchMaker;
import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.coincoinche.events.Event;
import com.coincoinche.events.EventType;
import com.coincoinche.events.GameStartedEvent;
import com.coincoinche.events.PlayerJoinedLobbyEvent;

import java.util.List;
import java.util.UUID;

import com.coincoinche.model.User;
import com.coincoinche.repositories.UserRepository;
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
  @Autowired private GameController gameController;
  @Autowired private UserRepository userRepository;

  private MatchMaker matchMaker;

  public LobbyController() {
    this.matchMaker = new MatchMaker(this);
  }

  /**
   * Endpoint called to register the player and find other players to start a new game.
   *
   * @param message - incoming message.
   * @return message acknowledging reception.
   */
  @MessageMapping("/lobby/join")
  @SendTo("/topic/lobby")
  public Event joinLobby(@Payload PlayerJoinedLobbyEvent message) {
    logger.debug(
        String.format(
            "Received message %s, username %s", message.getType(), message.getUsername()));

    if (!message.getType().equals(EventType.PLAYER_JOINED_LOBBY)) {
      return new Event(EventType.INVALID_MESSAGE);
    }

    // Create user if not exists.
    List<User> users = this.userRepository.findByUsername(message.getUsername());
    if (users.size() == 0) {
      this.userRepository.save(new User(message.getUsername()));
    }

    this.matchMaker.register(message.getUsername());

    return new Event(EventType.SUCCESS);
  }

  /**
   * Send a signal to all player that the game started.
   *
   * @param usernames - players usernames.
   */
  public void gameStart(String[] usernames) {
    if (usernames.length != 4) {
      throw new IllegalArgumentException("The game must start with exactly 4 players.");
    }

    Player player1 = new Player(usernames[0]);
    Player player2 = new Player(usernames[1]);
    Player player3 = new Player(usernames[2]);
    Player player4 = new Player(usernames[3]);

    Team redTeam = new Team(player1, player2, Team.Color.RED);
    Team blueTeam = new Team(player3, player4, Team.Color.BLUE);

    CoincheGame game = new CoincheGame(redTeam, blueTeam);

    String gameId = UUID.randomUUID().toString();

    this.template.convertAndSend("/topic/lobby", new GameStartedEvent(gameId));
    this.gameController.registerNewGame(gameId, game);
  }
}
