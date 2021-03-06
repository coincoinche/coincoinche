package com.coincoinche.websockets.controllers;

import com.coincoinche.MatchMaker;
import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.coincoinche.model.User;
import com.coincoinche.ratingplayer.EloPlayer;
import com.coincoinche.repositories.UserRepository;
import com.coincoinche.websockets.events.EventType;
import com.coincoinche.websockets.events.JoinedLobbyEvent;
import com.coincoinche.websockets.events.QuitLobbyEvent;
import com.coincoinche.websockets.messages.GameStartedMessage;
import com.coincoinche.websockets.messages.InvalidEventMessage;
import com.coincoinche.websockets.messages.Message;
import com.coincoinche.websockets.messages.SuccessMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
   * @param event - incoming event.
   * @return message acknowledging reception.
   */
  @MessageMapping("/lobby/join")
  @SendTo("/topic/lobby")
  public Message joinLobby(@Payload JoinedLobbyEvent event) {
    logger.debug("Received event {}, username {}", event.getType(), event.getUsername());

    if (!event.getType().equals(EventType.JOINED_LOBBY)) {
      return new InvalidEventMessage();
    }

    // Create user if they don't exist (for guest users)
    List<User> users = this.userRepository.findByUsername(event.getUsername());
    if (users.size() == 0) {
      this.userRepository.save(new User(event.getUsername()));
    }

    this.matchMaker.register(event.getUsername());

    return new SuccessMessage();
  }

  /**
   * Endpoint called to remove the player from the lobby.
   *
   * @param event - incoming event.
   * @return message acknowledging reception.
   */
  @MessageMapping("/lobby/quit")
  @SendTo("/topic/lobby")
  public Message quitLobby(@Payload QuitLobbyEvent event) {
    logger.debug("Received event {}, username {}", event.getType(), event.getUsername());

    if (!event.getType().equals(EventType.QUIT_LOBBY)) {
      return new InvalidEventMessage();
    }

    this.matchMaker.remove(event.getUsername());

    return new SuccessMessage();
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

    Team redTeam = new Team(player1, player3);
    Team blueTeam = new Team(player2, player4);

    CoincheGame game = new CoincheGame(redTeam, blueTeam);

    String gameId = UUID.randomUUID().toString();
    String[] orderedUsernames =
        game.getPlayers().stream()
            .map(Player::getUsername)
            .collect(Collectors.toList())
            .toArray(new String[4]);

    List<EloPlayer> eloPlayers = new ArrayList<>();
    for (String username : orderedUsernames) {
      List<User> users = userRepository.findByUsername(username);
      if (users.isEmpty()) {
        throw new RuntimeException(String.format("%s user not found", username));
      }
      eloPlayers.add(new EloPlayer(users.get(0)));
    }
    this.template.convertAndSend("/topic/lobby", new GameStartedMessage(gameId, eloPlayers));
    this.gameController.registerNewGame(gameId, game);
  }
}
