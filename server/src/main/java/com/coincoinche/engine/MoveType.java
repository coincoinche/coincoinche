package com.coincoinche.engine;

public enum MoveType {
  SPECIAL_BIDDING("SPECIAL_BIDDING"),
  CONTRACT_BIDDING("CONTRACT_BIDDING");

  private String type;

  MoveType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
