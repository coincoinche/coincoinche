package com.coincoinche.websockets.messages;

import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.CoincheGameRound;
import com.coincoinche.engine.GameState;
import com.coincoinche.engine.Move;
import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Message to send when the state has changed. */
public class NewStateMessage extends Message {

  private static final Logger logger = LoggerFactory.getLogger(NewStateMessage.class);
  private CoincheGame game;
  private Player player;

  /**
   * Create a message for notifying a new game state.
   *
   * @param game is the global coinche game whose state is updated.
   * @param player is the player who receives the message (some information from the state is
   *     censored, for instance other players' cards).
   */
  public NewStateMessage(CoincheGame game, Player player) {
    super();
    this.game = game;
    this.player = player;
  }

  @Override
  public String getMessageType() {
    return "NEW_STATE";
  }

  private String getPlayerCardsJson(Player player) {
    List<Card> cards = player.getCards();
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("[");
    for (Card card : cards) {
      if (player.equals(this.player)) {
        stringBuffer.append(String.format("\"%s\"", card.getShortName()));
      } else {
        // hide cards from other players
        stringBuffer.append("\"??\"");
      }
      if (cards.indexOf(card) < cards.size() - 1) {
        stringBuffer.append(",");
      }
    }
    stringBuffer.append("]");
    return stringBuffer.toString();
  }

  private String getCardsJson() {
    List<Player> players = game.getPlayers();
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("{");
    for (Player player : players) {
      stringBuffer.append(
          String.format("\"%s\":%s", player.getUsername(), getPlayerCardsJson(player)));
      if (players.indexOf(player) < players.size() - 1) {
        stringBuffer.append(",");
      }
    }
    stringBuffer.append("}");
    return stringBuffer.toString();
  }

  private String getMovesJson() {
    CoincheGameRound round = game.getCurrentRound();
    List<Move> legalMoves = round.getLegalMoves();
    if (!player.equals(round.getCurrentPlayer())) {
      return "[]";
    }
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("[");
    for (Move move : legalMoves) {
      stringBuffer.append(String.format("\"%s\"", move));
      if (legalMoves.indexOf(move) < legalMoves.size() - 1) {
        stringBuffer.append(",");
      }
    }
    stringBuffer.append("]");
    return stringBuffer.toString();
  }

  private String getScoresJson() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("{");
    Team playerTeam;
    Team otherTeam;
    if (game.getRedTeam().getPlayers().contains(player)) {
      playerTeam = (Team) game.getRedTeam();
      otherTeam = (Team) game.getBlueTeam();
    } else {
      playerTeam = (Team) game.getBlueTeam();
      otherTeam = (Team) game.getRedTeam();
    }
    int youPoints = playerTeam.getPoints();
    int themPoints = otherTeam.getPoints();
    stringBuffer.append(String.format("\"you\":%s,", youPoints));
    stringBuffer.append(String.format("\"them\":%s", themPoints));
    stringBuffer.append("}");
    return stringBuffer.toString();
  }

  @Override
  public String getJsonContent() {
    GameState state = game.getCurrentRound().getState();
    ObjectMapper mapper = new ObjectMapper();
    String stateJson = null;
    try {
      stateJson = mapper.writeValueAsString(state);
    } catch (JsonProcessingException e) {
      logger.error("Can't encode game state to JSON: {}", state);
      e.printStackTrace();
    }
    return String.format(
        "{\"state\":%s,\"cards\":%s,\"moves\":%s,\"scores\":%s}",
        stateJson, getCardsJson(), getMovesJson(), getScoresJson());
  }
}
