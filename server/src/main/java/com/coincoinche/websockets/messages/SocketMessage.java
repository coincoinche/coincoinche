package com.coincoinche.websockets.messages;

public class SocketMessage {
  protected MessageType type;

  public SocketMessage() {}

  public SocketMessage(MessageType type) {
    this.type = type;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }
}
