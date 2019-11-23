package com.coincoinche.websockets.events;

/** Existing types for events. */
public enum EventType {
  JOINED_LOBBY("JOINED_LOBBY"),
  QUIT_LOBBY("QUIT_LOBBY"),
  LOG_IN("LOG_IN"),
  SIGN_UP("SIGN_UP");

  private String type;

  EventType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
