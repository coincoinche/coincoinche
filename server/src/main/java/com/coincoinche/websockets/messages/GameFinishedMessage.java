package com.coincoinche.websockets.messages;

/** Message sent when the game has finished. */
public class GameFinishedMessage extends Message {

  private boolean win;

  /**
   * Create a message to notify the game ending.
   *
   * @param win is a boolean indicating if the message is for a winning team.
   */
  public GameFinishedMessage(boolean win) {
    super();
    this.win = win;
  }

  @Override
  public String getMessageType() {
    return "GAME_FINISHED";
  }

  @Override
  public String getJsonContent() {
    return String.format("{\"win\":%s}", win);
  }
}
