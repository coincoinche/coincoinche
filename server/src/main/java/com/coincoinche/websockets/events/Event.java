package com.coincoinche.websockets.events;

/** Event represents a message sent from the client via websocket. */
public class Event {

  protected EventType type;

  public Event() {}

  public Event(EventType type) {
    this.type = type;
  }

  public EventType getType() {
    return type;
  }
}
