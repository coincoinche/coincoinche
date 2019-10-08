package com.coincoinche.websockets.messages;

public class SearchGame extends SocketMessage {
  public SearchGame() {
    super(MessageType.JOIN_LOBBY);
  }
}
