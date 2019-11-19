package com.coincoinche.engine.contracts;

import static com.coincoinche.engine.CoincheGame.MAX_TRICKS_POINTS;

import com.coincoinche.engine.GameStatePlaying;
import com.coincoinche.engine.cards.Suit;

/** Contract representing a capot. */
public class ContractCapot extends Contract {

  private static final int CAPOT_VALUE = 250;

  ContractCapot(Suit suit) {
    this.suit = suit;
  }

  @Override
  public boolean isSuccessful(GameStatePlaying state) {
    return player.getTeam().getPlayers().stream().mapToInt(state::getTrickPointsForPlayer).sum()
        == MAX_TRICKS_POINTS;
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
