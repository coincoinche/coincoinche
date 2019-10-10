package com.coincoinche.events;

public class PlayerJoinedLobbyEvent extends Event {
  private String username;

  public PlayerJoinedLobbyEvent() {
    super(EventType.PLAYER_JOINED_LOBBY);
  }

  public PlayerJoinedLobbyEvent(String username) {
    super(EventType.PLAYER_JOINED_LOBBY);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
