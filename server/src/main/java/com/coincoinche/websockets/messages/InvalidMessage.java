package com.coincoinche.websockets.messages;

public class InvalidMessage extends SocketMessage {
  public InvalidMessage() {
    super(MessageType.INVALID_MESSAGE);
  }
}
