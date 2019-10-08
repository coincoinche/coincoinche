package com.coincoinche.websockets;

public enum MessageType {
  HELLO("HELLO");

  private String type;

  MessageType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
