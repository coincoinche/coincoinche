package com.coincoinche.engine;

import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.events.CardPlayedEvent;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;

/**
 * MovePlaying represents a move during the playing phase of the game. It is basically a card
 * belonging to a player's hand.
 */
public class MovePlaying extends Move implements Comparable<MovePlaying> {

  private Card card;

  private MovePlaying(Card card) {
    this.card = card;
  }

  /**
   * Create a playing move corresponding to a card.
   *
   * @param card represents the card that the player plays.
   * @return the newly constructed playing move.
   */
  public static MovePlaying cardMove(Card card) {
    return new MovePlaying(card);
  }

  @Override
  public int compareTo(MovePlaying o) {
    return this.card.compareTo(o.card);
  }

  @Override
  protected void applyOnRoundState(GameState state, Player player) throws IllegalMoveException {
    if (!(state instanceof GameStatePlaying)) {
      throw new IllegalMoveException(this + " must be applied to a playing state");
    }
    GameStatePlaying playingGameState = (GameStatePlaying) state;
    List<Move> legalMoves = playingGameState.getLegalMoves();
    if (!legalMoves.contains(this)) {
      throw new IllegalMoveException(this + " is not legal on state " + playingGameState);
    }
    Trick currentTrick = playingGameState.getCurrentTrick();
    currentTrick.add(player, card);
    if (!currentTrick.isComplete()) {
      return;
    }
    // the trick is complete: close it
    playingGameState.closeTrick();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof MovePlaying)) {
      return false;
    }
    MovePlaying otherMove = (MovePlaying) obj;
    return this.card.equals(otherMove.card);
  }

  @Override
  public String toString() {
    return card.toString();
  }

  @Override
  @JsonValue
  public String toJson() {
    return this.card.getShortName();
  }

  /**
   * Create a playing move from a CardPlayedEvent event.
   *
   * @param event - card played event from the client.
   * @return the newly constructed bidding move.
   */
  public static MovePlaying fromEvent(CardPlayedEvent event) {
    return cardMove(Card.valueOfByShortName(event.getCard()));
  }
}
