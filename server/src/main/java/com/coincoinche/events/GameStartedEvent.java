package com.coincoinche.events;

public class GameStartedEvent extends Event {
  private String gameId;

  public GameStartedEvent(String gameId) {
    super(EventType.GAME_STARTED);
    this.gameId = gameId;
  }

  public String getGameId() {
    return gameId;
  }
}
