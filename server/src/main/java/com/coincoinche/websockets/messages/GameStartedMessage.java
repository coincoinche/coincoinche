package com.coincoinche.websockets.messages;

import com.coincoinche.ratingplayer.EloPlayer;

/** Message sent when the game starts. */
public class GameStartedMessage extends Message {

  private String gameId;
  private EloPlayer[] users;

  /**
   * Message for the beginning of the game.
   *
   * @param gameId is the unique ID of the game.
   * @param users users taking part in the game, in the order defined by the game engine.
   */
  public GameStartedMessage(String gameId, EloPlayer[] users) {
    super();
    this.gameId = gameId;
    this.users = users;
  }

  @Override
  public String getMessageType() {
    return "GAME_STARTED";
  }

  private String getJsonUsers() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("[");
    int index = 0;
    for (EloPlayer eloPlayer : users) {
      stringBuffer.append(eloPlayer.toJson());
      index++;
      // TODO nockty replace users by an ArrayList, it'll be cleaner
      // assume there are 4 players in the game
      if (index < 4) {
        stringBuffer.append(",");
      }
    }
    stringBuffer.append("]");
    return stringBuffer.toString();
  }

  @Override
  public String getJsonContent() {
    return String.format("{\"gameId\":\"%s\",\"users\":%s}", gameId, getJsonUsers());
  }
}
