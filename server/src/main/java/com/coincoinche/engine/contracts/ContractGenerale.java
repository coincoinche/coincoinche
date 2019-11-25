package com.coincoinche.engine.contracts;

import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.GameStatePlaying;
import com.coincoinche.engine.cards.Suit;

/** Contract representing a generale. */
public class ContractGenerale extends Contract {

  private static final int GENERALE_VALUE = 500;

  protected ContractGenerale(Suit suit) {
    this.suit = suit;
  }

  @Override
  public boolean isSuccessful(GameStatePlaying state) {
    return state.getTrickPointsForPlayer(player) == CoincheGame.getMaxTricksPoints();
  }

  @Override
  public int getEarnedPoints() {
    return GENERALE_VALUE;
  }

  @Override
  public String getValueShortName() {
    return String.valueOf(GENERALE_VALUE);
  }
}
