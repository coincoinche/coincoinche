package com.coincoinche.websockets.controllers;

import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.IllegalMoveException;
import com.coincoinche.engine.Move;
import com.coincoinche.engine.MoveBidding;
import com.coincoinche.engine.MovePlaying;
import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.coincoinche.events.*;
import com.coincoinche.store.GameStore;
import com.coincoinche.store.InMemoryGameStore;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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

  private String getBroadcastTopicPath(String gameId) {
    return String.format("/topic/game/%s", gameId);
  }

  private void notifyPlayerTurnStarted(String gameId, String username) {
    CoincheGame game = this.store.getGame(gameId);
    int newPlayerIndex = game.getCurrentRound().getCurrentPlayerIndex();

    if (username.equals(game.getCurrentRound().getCurrentPlayer().getUsername())) {
      List<Move> legalMoves = game.getCurrentRound().getLegalMoves();
      String[] authorisedPlaysJson = new String[legalMoves.size()];
      for (int i = 0; i < legalMoves.size(); i++) {
        authorisedPlaysJson[i] = legalMoves.get(i).toJson();
      }
      TurnStartedEvent eventForNextPlayer;
      TurnStartedEvent eventForOtherPlayers;
      if (game.getCurrentRoundPhase() == CoincheGame.Phase.BIDDING) {
        eventForNextPlayer = new BiddingTurnStartedEvent(authorisedPlaysJson, newPlayerIndex);
        eventForOtherPlayers = new BiddingTurnStartedEvent(new String[0], newPlayerIndex);
      } else {
        eventForNextPlayer = new PlayingTurnStartedEvent(authorisedPlaysJson, newPlayerIndex);
        eventForOtherPlayers = new PlayingTurnStartedEvent(new String[0], newPlayerIndex);
      }

      // send the authorised plays to the next player.
      this.template.convertAndSend(getTopicPath(gameId, username), eventForNextPlayer);

      // signal to the other players who is currently playing.
      List<Player> players = game.getPlayers();
      for (Player player : players) {
        if (!player.getUsername().equals(username)) {
          this.template.convertAndSend(
              getTopicPath(gameId, player.getUsername()), eventForOtherPlayers);
        }
      }
    }
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
        getTopicPath(gameId, username), new RoundPhaseStartedEvent(game.getCurrentRoundPhase()));

    this.notifyPlayerTurnStarted(gameId, username);
  }

  /**
   * To be called by the client when the player bids.
   *
   * @param gameId - id of the game
   * @param username - username of the player
   */
  @MessageMapping("/game/{gameId}/player/{username}/bid")
  public void makeBid(
      @DestinationVariable String gameId,
      @DestinationVariable String username,
      @Payload PlayerBadeEvent event) {
    CoincheGame game = this.store.getGame(gameId);
    MoveBidding move = MoveBidding.fromEvent(event);
    try {
      move.applyOnGame(game);
      this.template.convertAndSend(getBroadcastTopicPath(gameId), event);
    } catch (IllegalMoveException e) {
      e.printStackTrace();
      this.template.convertAndSend(
          getTopicPath(gameId, username), new Event(EventType.INVALID_MESSAGE));

      try {
        // make the player pass if the bidding move is invalid.
        MoveBidding.passMove().applyOnGame(game);
        this.template.convertAndSend(
            getBroadcastTopicPath(gameId), new PlayerBadeEvent(MoveBidding.Special.PASS));
      } catch (IllegalMoveException illegalPassMoveException) {
        illegalPassMoveException.printStackTrace();
      }
    }

    if (game.getCurrentRoundPhase() == CoincheGame.Phase.MAIN) {
      this.template.convertAndSend(
          getBroadcastTopicPath(gameId), new RoundPhaseStartedEvent(game.getCurrentRoundPhase()));
    }

    this.notifyPlayerTurnStarted(gameId, game.getCurrentRound().getCurrentPlayer().getUsername());
  }

  /**
   * To be called by the client when the player plays a card.
   *
   * @param gameId - id of the game
   * @param username - username of the player
   */
  @MessageMapping("/game/{gameId}/player/{username}/play")
  public void playCard(
      @DestinationVariable String gameId,
      @DestinationVariable String username,
      @Payload CardPlayedEvent event) {
    CoincheGame game = this.store.getGame(gameId);

    MovePlaying move = MovePlaying.fromEvent(event);
    try {
      GameResult<Team> result = move.applyOnGame(game);
      this.template.convertAndSend(getBroadcastTopicPath(gameId), event);

      if (result.isFinished()) {
        for (Player player : game.getPlayers()) {
          if (result.getWinnerTeam().getPlayers().contains(player)) {
            this.template.convertAndSend(
                getTopicPath(gameId, player.getUsername()), new GameFinishedEvent(true));
          } else {
            this.template.convertAndSend(
                getTopicPath(gameId, player.getUsername()), new GameFinishedEvent(false));
          }
        }
      }

    } catch (IllegalMoveException e) {
      e.printStackTrace();
      this.template.convertAndSend(
          getTopicPath(gameId, username), new Event(EventType.INVALID_MESSAGE));

      try {
        // make the player play the first authorised card
        Move firstLegalMove = game.getCurrentRound().getLegalMoves().get(0);
        firstLegalMove.applyOnGame(game);

        this.template.convertAndSend(
            getBroadcastTopicPath(gameId), new CardPlayedEvent(firstLegalMove.toJson()));
      } catch (IllegalMoveException illegalPassMoveException) {
        illegalPassMoveException.printStackTrace();
      }
    }

    if (game.getCurrentRoundPhase() == CoincheGame.Phase.BIDDING) {
      this.template.convertAndSend(
          getBroadcastTopicPath(gameId), new RoundPhaseStartedEvent(game.getCurrentRoundPhase()));
    }

    this.notifyPlayerTurnStarted(gameId, game.getCurrentRound().getCurrentPlayer().getUsername());
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
