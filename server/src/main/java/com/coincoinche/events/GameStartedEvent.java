package com.coincoinche.events;

public class GameStartedEvent extends Event {
  private String gameId;
  private String[] usernames;

  /**
   * This events marks the start of a new game.
   *
   * @param gameId - the id of the game.
   * @param usernames - the usernames of all the players, in the ordered defined by the game engine.
   */
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
