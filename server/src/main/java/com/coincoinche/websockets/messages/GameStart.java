package com.coincoinche.websockets.messages;

public class GameStart extends SocketMessage {
  public GameStart() {
    super(MessageType.GAME_START);
  }
}
