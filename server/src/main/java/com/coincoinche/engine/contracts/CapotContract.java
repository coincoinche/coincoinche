package com.coincoinche.engine.contracts;

import static com.coincoinche.engine.CoincheGame.MAX_TRICKS_POINTS;

import com.coincoinche.engine.GameStatePlaying;
import com.coincoinche.engine.cards.Suit;

/** Contract representing a capot. */
public class CapotContract extends Contract {

  private static final int CAPOT_VALUE = 250;

  CapotContract(Suit suit) {
    this.suit = suit;
  }

  @Override
  public boolean isSuccessful(GameStatePlaying state) {
    return player.getTeam().getPlayers().stream().mapToInt(state::getPointsForPlayer).sum()
        == MAX_TRICKS_POINTS;
  }

  @Override
  public int getEarnedPoints() {
    return CAPOT_VALUE;
  }

  @Override
  public String getShortName() {
    return "CAP";
  }
}
