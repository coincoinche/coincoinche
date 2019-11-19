package com.coincoinche.websockets.events;

public class JoinedLobbyEvent extends Event {
  private String username;

  public JoinedLobbyEvent() {
    super(EventType.JOINED_LOBBY);
  }

  public JoinedLobbyEvent(String username) {
    super(EventType.JOINED_LOBBY);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
