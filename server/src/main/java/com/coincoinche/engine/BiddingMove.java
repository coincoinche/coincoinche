package com.coincoinche.engine;

import com.coincoinche.engine.teams.Player;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;

/**
 * BiddingMove represents a move during the bidding phase of the game. It can be:
 *
 * <ul>
 *   <li>pass
 *   <li>coinche
 *   <li>surcoinche
 *   <li>a contract higher than the previous one
 * </ul>
 */
public class BiddingMove extends Move implements Comparable<BiddingMove> {

  public enum Special {
    PASS,
    COINCHE,
    SURCOINCHE;
  }

  private Contract contract;
  private Special specialMove;

  private BiddingMove(Contract contract, Special specialMove) {
    this.contract = contract;
    this.specialMove = specialMove;
  }

  /**
   * Create a bidding move corresponding to a contract.
   *
   * @param contract represents the contract claimed during the move.
   * @return the newly constructed bidding move.
   */
  public static BiddingMove contractMove(Contract contract) {
    return new BiddingMove(contract, null);
  }

  /**
   * Create a bidding move corresponding to a player passing.
   *
   * @return the newly constructed bidding move.
   */
  public static BiddingMove passMove() {
    return new BiddingMove(null, Special.PASS);
  }

  /**
   * Create a bidding move corresponding to a coinche.
   *
   * @return the newly constructed bidding move.
   */
  public static BiddingMove coincheMove() {
    return new BiddingMove(null, Special.COINCHE);
  }

  /**
   * Create a bidding move corresponding to a surcoinche.
   *
   * @return the newly constructed bidding move.
   */
  public static BiddingMove surcoincheMove() {
    return new BiddingMove(null, Special.SURCOINCHE);
  }

  @Override
  protected void applyOnRoundState(GameState state) throws IllegalMoveException {
    if (!(state instanceof GameStateBidding)) {
      throw new IllegalMoveException(this + " must be applied to a bidding state");
    }
    GameStateBidding biddingGameState = (GameStateBidding) state;
    List<Move> legalMoves = biddingGameState.getLegalMoves();
    if (!legalMoves.contains(this)) {
      throw new IllegalMoveException(this + " is not legal on state " + state);
    }
    if (specialMove != null) {
      switch (specialMove) {
        case PASS:
          break;
        case COINCHE:
          biddingGameState.setCoinched(true);
          break;
        case SURCOINCHE:
          biddingGameState.setSurcoinched(true);
          break;
        default:
          break;
      }
      return;
    }
    biddingGameState.setHighestBidding(contract.withPlayer(biddingGameState.getCurrentPlayer()));
  }

  @Override
  public int compareTo(BiddingMove o) {
    // Special moves: PASS < COINCHE < SURCOINCHE
    if (this.specialMove != null && o.specialMove != null) {
      return this.specialMove.compareTo(o.specialMove);
    }
    if (this.specialMove != null) {
      return -1;
    }
    if (o.specialMove != null) {
      return 1;
    }
    // Both moves are contract moves: compare their value
    if (o.contract.isHigherThan(this.contract)) {
      return -1;
    }
    if (this.contract.isHigherThan(o.contract)) {
      return 1;
    }
    // Contracts have same value: compare their color
    return this.contract.getSuit().compareTo(o.contract.getSuit());
  }

  public Contract getContract() {
    return contract;
  }

  public boolean isPass() {
    return specialMove != null && specialMove.equals(Special.PASS);
  }

  public boolean isCoinche() {
    return specialMove != null && specialMove.equals(Special.COINCHE);
  }

  public boolean isSurcoinche() {
    return specialMove != null && specialMove.equals(Special.SURCOINCHE);
  }

  /**
   * Attach a player to the move's contract.
   *
   * @param player is the player to attach.
   */
  public void attachPlayer(Player player) {
    this.contract.setPlayer(player);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BiddingMove)) {
      return false;
    }
    BiddingMove otherMove = (BiddingMove) obj;
    if (this.specialMove != null || otherMove.specialMove != null) {
      return this.specialMove == otherMove.specialMove;
    }
    return this.contract.equals(otherMove.contract);
  }

  @Override
  public String toString() {
    if (specialMove != null) {
      return specialMove.toString();
    }
    return contract.toString();
  }

  @JsonValue
  public String toJson() {
    String specialMoveJsonTemplate = "{\"special\":\"%s\"}";

    if (isCoinche()) {
      return String.format(specialMoveJsonTemplate, Special.COINCHE);
    }
    if (isSurcoinche()) {
      return String.format(specialMoveJsonTemplate, Special.SURCOINCHE);
    }
    if (isPass()) {
      return String.format(specialMoveJsonTemplate, Special.PASS);
    }

    return getContract().toJson();
  }
}
