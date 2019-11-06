package com.coincoinche.events;

public enum EventType {
  // communication utility events
  INVALID_MESSAGE("INVALID_MESSAGE"),
  SUCCESS("SUCCESS"),
  CLIENT_READY("CLIENT_READY"),

  // player events
  PLAYER_JOINED_LOBBY("PLAYER_JOINED_LOBBY"),
  PLAYER_BADE("PLAYER_BADE"),
  CARD_PLAYED("CARD_PLAYED"),

  // server events
  GAME_STARTED("GAME_STARTED"),
  GAME_FINISHED("GAME_FINISHED"),
  ROUND_STARTED("ROUND_STARTED"),
  ROUND_PHASE_STARTED("ROUND_PHASE_STARTED"),
  BIDDING_TURN_STARTED("BIDDING_TURN_STARTED"),
  PLAYING_TURN_STARTED("PLAYING_TURN_STARTED");

  private String type;

  EventType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
