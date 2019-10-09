package com.coincoinche.events;

public enum EventType {
  // communication utility events
  INVALID_MESSAGE("INVALID_MESSAGE"),
  SUCCESS("SUCCESS"),
  CLIENT_READY("CLIENT_READY"),

  // player events
  PLAYER_JOINED_LOBBY("PLAYER_JOINED_LOBBY"),

  // server events
  GAME_STARTED("GAME_STARTED"),
  ROUND_STARTED("ROUND_STARTED");

  private String type;

  EventType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
