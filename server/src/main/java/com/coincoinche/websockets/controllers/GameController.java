package com.coincoinche.websockets.controllers;

import com.coincoinche.engine.BiddingMove;
import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.Move;
import com.coincoinche.engine.RoundPhase;
import com.coincoinche.events.RoundPhaseStartedEvent;
import com.coincoinche.events.RoundStartedEvent;
import com.coincoinche.events.TurnStartedEvent;
import com.coincoinche.store.GameStore;
import com.coincoinche.store.InMemoryGameStore;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {
  private static final Logger logger = LoggerFactory.getLogger(GameController.class);

  @Autowired private SimpMessagingTemplate template;
  private GameStore store;

  public GameController() {
    this.store = new InMemoryGameStore();
  }

  private String getTopicPath(String gameId, String username) {
    return String.format("/topic/game/%s/player/%s", gameId, username);
  }
  /**
   * To be called after the client loaded the game. Send its hand to the player.
   *
   * @param gameId - id of the game
   * @param username - username of the player
   */
  @MessageMapping("/game/{gameId}/player/{username}/ready")
  public void getNewHandForRound(
      @DestinationVariable String gameId, @DestinationVariable String username) {
    // TODO error handling if the game is not found
    CoincheGame game = this.store.getGame(gameId);

    this.template.convertAndSend(
        getTopicPath(gameId, username), new RoundStartedEvent(game.getPlayer(username).getCards()));

    this.template.convertAndSend(
        getTopicPath(gameId, username),
        new RoundPhaseStartedEvent(RoundPhase.BIDDING, game.getCurrentPlayerIndex()));

    if (username.equals(game.getCurrentPlayer().getUsername())) {
      List<Move> authorisedPlays = game.getCurrentRound().getLegalMoves();
      String[] authorisedPlaysJson = new String[authorisedPlays.size()];
      for (int i = 0; i < authorisedPlays.size(); i++) {
        authorisedPlaysJson[i] = ((BiddingMove) authorisedPlays.get(i)).toJson();
      }

      this.template.convertAndSend(
          getTopicPath(gameId, username), new TurnStartedEvent(authorisedPlaysJson));
    }
  }

  /**
   * Register a new game for later access.
   *
   * @param gameId - id of the game
   * @param game - CoincheGame object
   */
  public void registerNewGame(String gameId, CoincheGame game) {
    this.store.saveGame(gameId, game);
  }
}
