package com.coincoinche.engine;

public enum RoundPhase {
  BIDDING("BIDDING_PHASE"),
  MAIN("PLAYING_PHASE");

  private String phase;

  RoundPhase(String phase) {
    this.phase = phase;
  }

  public String getPhase() {
    return phase;
  }
}
