package com.coincoinche.engine.contracts;

import static com.coincoinche.engine.CoincheGame.MAX_TRICKS_POINTS;

import com.coincoinche.engine.GameStatePlaying;
import com.coincoinche.engine.cards.Suit;

/** Contract representing a generale. */
public class ContractGenerale extends Contract {

  private static final int GENERALE_VALUE = 500;

  ContractGenerale(Suit suit) {
    this.suit = suit;
  }

  @Override
  public boolean isSuccessful(GameStatePlaying state) {
    return state.getTrickPointsForPlayer(player) == MAX_TRICKS_POINTS;
  }

  @Override
  public int getEarnedPoints() {
    return GENERALE_VALUE;
  }

  @Override
  public String getShortName() {
    return "GEN";
  }
}
