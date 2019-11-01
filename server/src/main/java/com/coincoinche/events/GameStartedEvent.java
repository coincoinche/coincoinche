package com.coincoinche.events;

public class GameStartedEvent extends Event {
  private String gameId;
  private String[] usernames;

  public GameStartedEvent(String gameId, String[] usernames) {
    super(EventType.GAME_STARTED);
    this.gameId = gameId;
    this.usernames = usernames;
  }

  public String getGameId() {
    return gameId;
  }

  public String[] getUsernames() {
    return usernames;
  }
}
