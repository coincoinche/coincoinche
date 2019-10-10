package com.coincoinche.events;

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
