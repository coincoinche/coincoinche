package com.coincoinche.events;

public class TurnStartedEvent extends Event {
  private String[] authorizedPlays;

  public TurnStartedEvent(String[] authorizedPlays) {
    super(EventType.TURN_STARTED);
    this.authorizedPlays = authorizedPlays;
  }

  public String[] getAuthorizedPlays() {
    return authorizedPlays;
  }
}
