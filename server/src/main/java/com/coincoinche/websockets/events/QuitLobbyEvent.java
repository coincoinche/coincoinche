package com.coincoinche.websockets.events;

public class QuitLobbyEvent extends Event {
  private String username;

  public QuitLobbyEvent() {
    super(EventType.QUIT_LOBBY);
  }

  public QuitLobbyEvent(String username) {
    super(EventType.QUIT_LOBBY);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
