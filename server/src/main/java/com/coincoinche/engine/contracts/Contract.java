package com.coincoinche.engine.contracts;

import com.coincoinche.engine.GameStatePlaying;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.teams.Player;

/** Abstract class for contracts. */
public abstract class Contract {

  protected Player player;
  protected Suit suit;

  public abstract boolean isSuccessful(GameStatePlaying state);

  public abstract int getEarnedPoints();

  public abstract String getValueShortName();

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
    return this.getEarnedPoints() > other.getEarnedPoints();
  }

  /**
   * Check that two contracts are equal. <strong>Contracts don't need to belong to the same player
   * to be considered equal.</strong>
   *
   * @param obj another contract.
   * @return a boolean indicating if the contracts are equal.
   */
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
    return this.getEarnedPoints() == otherContract.getEarnedPoints();
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

  public String getShortName() {
    return String.format("%s%s", getValueShortName(), suit.getShortName());
  }

  @Override
  public String toString() {
    StringBuffer prettyString = new StringBuffer();
    if (player != null) {
      prettyString.append(player.toString());
      prettyString.append(":");
    }
    prettyString.append(getShortName());
    return prettyString.toString();
  }
}
