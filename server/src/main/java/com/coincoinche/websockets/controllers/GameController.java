package com.coincoinche.websockets.controllers;

import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.IllegalMoveException;
import com.coincoinche.engine.Move;
import com.coincoinche.engine.MoveFactory;
import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.coincoinche.ratingplayer.EloService;
import com.coincoinche.repositories.GameRepository;
import com.coincoinche.repositories.InMemoryGameRepository;
import com.coincoinche.websockets.messages.GameFinishedMessage;
import com.coincoinche.websockets.messages.NewStateMessage;
import java.util.List;
import java.util.Random;
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

  private static final String INDIVIDUAL_TOPIC_PATH = "/topic/game/%s/player/%s";

  @Autowired private SimpMessagingTemplate template;
  @Autowired private EloService eloService;
  private GameRepository repository;

  public GameController() {
    this.repository = new InMemoryGameRepository();
  }

  /**
   * Register a new game for later access.
   *
   * @param gameId - id of the game
   * @param game - CoincheGame object
   */
  public void registerNewGame(String gameId, CoincheGame game) {
    this.repository.saveGame(gameId, game);
  }

  private String getIndividualTopicPath(String gameId, String username) {
    return String.format(INDIVIDUAL_TOPIC_PATH, gameId, username);
  }

  private void pushStateToPlayer(String gameId, CoincheGame game, Player player) {
    NewStateMessage message = new NewStateMessage(game, player);
    this.template.convertAndSend(getIndividualTopicPath(gameId, player.getUsername()), message);
  }

  private void pushStateToAllPlayers(String gameId, CoincheGame game) {
    logger.debug("Game {}: pushing new state to all players", gameId);
    for (Player player : game.getPlayers()) {
      pushStateToPlayer(gameId, game, player);
    }
  }

  /**
   * Notify the client is ready. Send the state of the game to the player.
   *
   * @param gameId is the id of the game.
   * @param username is the username of the player.
   */
  @MessageMapping("/game/{gameId}/player/{username}/ready")
  public void getFirstGameState(
      @DestinationVariable String gameId, @DestinationVariable String username) {
    logger.debug("Game {}: received event READY by user {}", gameId, username);
    // TODO error handling if the game is not found
    CoincheGame game = this.repository.getGame(gameId);
    try {
      Player player = game.getPlayer(username);
      pushStateToPlayer(gameId, game, player);
    } catch (IllegalArgumentException e) {
      logger.error("{}: player {} not found in game {}", e, username, gameId);
      e.printStackTrace();
    }
  }

  /**
   * Route for making a move on the game.
   *
   * @param gameId is the id of the game.
   * @param username is the user making the move.
   * @param jsonMove is the JSON representation of the move being made.
   */
  @MessageMapping("/game/{gameId}/player/{username}/move")
  public void move(
      @DestinationVariable String gameId,
      @DestinationVariable String username,
      @Payload String jsonMove) {
    // TODO nockty: check that the move comes from the current player!
    logger.debug("Game {} User {}: received move {}", gameId, username, jsonMove);
    CoincheGame game = repository.getGame(gameId);
    // create legal move
    logger.debug("create legal move");
    Move move;
    try {
      move = MoveFactory.createMove(jsonMove);
    } catch (IllegalMoveException e) {
      // if the move is illegal, get a random legal move instead
      logger.warn("Game {}: {} sent an illegal move: {}", gameId, username, jsonMove);
      List<Move> legalMoves = game.getCurrentRound().getLegalMoves();
      move = legalMoves.get(new Random().nextInt(legalMoves.size()));
    }
    // apply it on game
    logger.debug("apply move on game");
    try {
      GameResult<Team> result = move.applyOnGame(game);
      if (result.isFinished()) {
        logger.debug("game is finished");
        terminateGame(gameId, game, result);
        return;
      }
      if (game.isNewRound()) {
        logger.debug("new round");
        pushStateToAllPlayers(gameId, game);
        return;
      }
      logger.debug("round continues");
      pushStateToAllPlayers(gameId, game);
    } catch (IllegalMoveException e) {
      // should never happen
      logger.error("Inconsistent error: legal move is illegal: {}", move);
      e.printStackTrace();
    }
  }

  private void terminateGame(String gameId, CoincheGame game, GameResult<Team> result) {
    logger.info("Game finished: {}", result);
    Team winnerTeam = result.getWinnerTeam();
    Team loserTeam = result.getLoserTeam();
    eloService.updateRatings(winnerTeam, loserTeam);
    for (Player player : game.getPlayers()) {
      if (winnerTeam.getPlayers().contains(player)) {
        this.template.convertAndSend(
            getIndividualTopicPath(gameId, player.getUsername()), new GameFinishedMessage(true));
      } else {
        this.template.convertAndSend(
            getIndividualTopicPath(gameId, player.getUsername()), new GameFinishedMessage(false));
      }
    }
    logger.debug("Removing game {} from memory", gameId);
    repository.removeGame(gameId);
  }
}
