package com.coincoinche.events;

public enum EventType {
  INVALID_MESSAGE("INVALID_MESSAGE"),
  SUCCESS("SUCCESS"),
  JOIN_LOBBY("JOIN_LOBBY"),
  GAME_START("GAME_START"),
  ROUND_START("ROUND_START");

  private String type;

  EventType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
