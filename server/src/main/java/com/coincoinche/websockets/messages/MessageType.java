package com.coincoinche.websockets.messages;

public enum MessageType {
  HELLO("HELLO"),
  INVALID_MESSAGE("INVALID_MESSAGE"),
  SUCCESS("SUCCESS"),
  JOIN_LOBBY("JOIN_LOBBY"),
  GAME_START("GAME_START");

  private String type;

  MessageType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
