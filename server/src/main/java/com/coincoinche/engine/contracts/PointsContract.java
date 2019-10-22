package com.coincoinche.engine.contracts;

import static com.coincoinche.engine.CoincheGame.MAX_TRICKS_POINTS;
import static java.lang.Math.max;

import com.coincoinche.engine.GameStatePlaying;
import com.coincoinche.engine.cards.Suit;
import java.util.ArrayList;
import java.util.List;

/** Contract representing a number of points. */
public class PointsContract extends Contract {

  private static final List<Integer> legalValues =
      new ArrayList<>(List.of(80, 90, 100, 110, 120, 130, 140, 150, 160));

  private int value;

  PointsContract(int value, Suit suit) throws IllegalArgumentException {
    if (!legalValues.contains(value)) {
      throw new IllegalArgumentException(value + " is not legal for a points contract");
    }
    this.value = value;
    this.suit = suit;
  }

  @Override
  public boolean isSuccessful(GameStatePlaying state) {
    return player.getTeam().getPlayers().stream().mapToInt(state::getTrickPointsForPlayer).sum()
        >= max(value, MAX_TRICKS_POINTS / 2 + 1);
  }

  @Override
  public int getEarnedPoints() {
    return value;
  }

  @Override
  public String getShortName() {
    return String.valueOf(value);
  }

  public static List<Integer> getLegalvalues() {
    return legalValues;
  }
}
