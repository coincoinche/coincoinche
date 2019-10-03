package com.coincoinche.engine;

import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.teams.Player;
import java.util.ArrayList;
import java.util.List;

/** Contract of the bidding phase of the game. */
public class Contract {

  private enum Value {
    EIGHTY(80),
    NINETY(90),
    HUNDRED(100),
    HUNDRED_TEN(110),
    HUNDRED_TWENTY(120),
    HUNDRED_THIRTY(130),
    HUNDRED_FOURTY(140),
    HUNDRED_FIFTY(150),
    HUNDRED_SIXTY(160),
    CAPOT(250),
    GENERALE(500);

    private final int points;

    private Value(int points) {
      this.points = points;
    }

    public static Value valueOfByPoints(int points) throws IllegalArgumentException {
      for (Value value : Value.values()) {
        // can't get CAPOT or GENERALE value from points
        if (value == CAPOT || value == GENERALE) {
          continue;
        }
        if (value.getPoints() == points) {
          return value;
        }
      }
      throw new IllegalArgumentException("Value with points " + points + " doesn't exist.");
    }

    public int getPoints() {
      return points;
    }

    @Override
    public String toString() {
      if (this == CAPOT) {
        return "CAP";
      }
      if (this == GENERALE) {
        return "GEN";
      }
      return String.format("%d", this.points);
    }
  }

  private Value value;
  // TODO nockty: figure out how to change this when we handle all trumps & no trumps
  private Suit suit;
  private Player player;

  private Contract(Value value, Suit suit) {
    this.value = value;
    this.suit = suit;
  }

  /**
   * Create a contract based on points.
   *
   * @param points is a value between 80 and 160 representing the points claimed by the contract.
   * @param suit is the suit of the contract.
   * @return the newly constructed contract.
   */
  public static Contract pointsContract(int points, Suit suit) throws IllegalArgumentException {
    return new Contract(Value.valueOfByPoints(points), suit);
  }

  /**
   * Create a capot contract.
   *
   * @param suit is the suit of the contract.
   * @return the newly constructed contract.
   */
  public static Contract capotContract(Suit suit) {
    return new Contract(Value.CAPOT, suit);
  }

  /**
   * Create a generale contract.
   *
   * @param suit is the suit of the contract.
   * @return the newly constructed contract.
   */
  public static Contract generaleContract(Suit suit) {
    return new Contract(Value.GENERALE, suit);
  }

  /**
   * Generate every possible contract in the game.
   *
   * @return the list of all possible contracts.
   */
  public static List<Contract> generateAllContracts() {
    List<Contract> allContracts = new ArrayList<>();
    for (Suit suit : Suit.values()) {
      for (Value value : Value.values()) {
        allContracts.add(new Contract(value, suit));
      }
    }
    return allContracts;
  }

  /**
   * Check if the contract is strictly higher than the other contract.
   *
   * @param other is the other contract that should be checked.
   * @return true if the other contract is strictly lower.
   */
  public boolean isHigherThan(Contract other) {
    if (other == null) {
      return true;
    }
    return this.value.compareTo(other.value) > 0;
  }

  /**
   * Attach a player to the contract.
   *
   * @param player player to add to the contract.
   * @return the contract with the newly added player.
   */
  public Contract withPlayer(Player player) {
    this.player = player;
    return this;
  }

  public Suit getSuit() {
    return suit;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Contract)) {
      return false;
    }
    Contract otherContract = (Contract) obj;
    if (!this.suit.equals(otherContract.suit)) {
      return false;
    }
    return this.value == otherContract.value;
  }

  @Override
  public String toString() {
    StringBuffer prettyString = new StringBuffer();
    if (player != null) {
      prettyString.append(player.toString());
      prettyString.append(": ");
    }
    prettyString.append(this.value.toString());
    prettyString.append(suit.toString());
    return prettyString.toString();
  }
}
