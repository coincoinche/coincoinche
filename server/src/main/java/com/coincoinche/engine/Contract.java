package com.coincoinche.engine;

import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.teams.Player;
import java.util.ArrayList;
import java.util.List;

/** Contract of the bidding phase of the game. */
public class Contract {

  private int value;
  private boolean capot;
  private boolean generale;
  // TODO nockty: figure out how to change this when we handle all trumps & no trumps
  private Suit suit;
  private Player player;

  private Contract(int value, boolean capot, boolean generale, Suit suit) {
    this.value = value;
    this.capot = capot;
    this.generale = generale;
    this.suit = suit;
  }

  /**
   * Create a contract based on points.
   *
   * @param value is a value between 80 and 160 representing the points claimed by the contract.
   * @param suit is the suit of the contract.
   * @return the newly constructed contract.
   */
  public static Contract pointsContract(int value, Suit suit) {
    return new Contract(value, false, false, suit);
  }

  /**
   * Create a capot contract.
   *
   * @param suit is the suit of the contract.
   * @return the newly constructed contract.
   */
  public static Contract capotContract(Suit suit) {
    return new Contract(-1, true, false, suit);
  }

  /**
   * Create a generale contract.
   *
   * @param suit is the suit of the contract.
   * @return the newly constructed contract.
   */
  public static Contract generaleContract(Suit suit) {
    return new Contract(-1, false, true, suit);
  }

  /**
   * Generate every possible contract in the game.
   *
   * @return the list of all possible contracts.
   */
  public static List<Contract> generateAllContracts() {
    List<Contract> allContracts = new ArrayList<>();
    for (Suit suit : Suit.values()) {
      // points contracts
      for (int value = 80; value <= 160; value += 10) {
        allContracts.add(pointsContract(value, suit));
      }
      // capot & generale
      allContracts.add(capotContract(suit));
      allContracts.add(generaleContract(suit));
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
    // handle case where other is null
    if (other == null) {
      return true;
    }
    // special cases: generale > capot > all other values
    if (other.generale) {
      return false;
    }
    if (this.generale) {
      return true;
    }
    if (other.capot) {
      return false;
    }
    if (this.capot) {
      return true;
    }
    // general case: value must be strictly higher
    return this.value > other.value;
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

  /**
   * Get the contract's value. The value of capot is 250. The value of generale is 500.
   *
   * @return an int representing the contract's value
   */
  public int getValue() {
    // TODO nockty change capot and generale to special move enum w/ their value
    if (generale) {
      return 500;
    }
    if (capot) {
      return 250;
    }
    return value;
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
    return this.getValue() == otherContract.getValue();
  }

  @Override
  public String toString() {
    StringBuffer prettyString = new StringBuffer();
    if (player != null) {
      prettyString.append(player.toString());
      prettyString.append(": ");
    }
    if (generale) {
      prettyString.append("GEN");
    } else if (capot) {
      prettyString.append("CAP");
    } else {
      prettyString.append(value);
    }
    prettyString.append(suit.toString());
    return prettyString.toString();
  }
}
