package com.coincoinche.websockets.messages;

public enum MessageType {
  HELLO("HELLO"),
  SEARCH_GAME("SEARCH_GAME");

  private String type;

  MessageType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
