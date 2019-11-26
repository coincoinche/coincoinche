package com.coincoinche.engine.contracts;

import com.coincoinche.engine.CoincheGame;
import com.coincoinche.engine.GameStatePlaying;
import com.coincoinche.engine.cards.Suit;

/** Contract representing a capot. */
public class ContractCapot extends Contract {

  private static final int CAPOT_VALUE = 250;

  protected ContractCapot(Suit suit) {
    this.suit = suit;
  }

  @Override
  public boolean isSuccessful(GameStatePlaying state) {
    return player.getTeam().getPlayers().stream().mapToInt(state::getTrickPointsForPlayer).sum()
        == CoincheGame.getMaxTricksPoints();
  }

  @Override
  public int getEarnedPoints() {
    return CAPOT_VALUE;
  }

  @Override
  public String getValueShortName() {
    return String.valueOf(CAPOT_VALUE);
  }
}
