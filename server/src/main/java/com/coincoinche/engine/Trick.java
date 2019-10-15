package com.coincoinche.engine;

import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.cards.ValuedCard;
import com.coincoinche.engine.cards.ValuedCardComparator;
import com.coincoinche.engine.teams.Player;
import java.util.ArrayList;
import java.util.List;

/** Trick of the playing phase of the game. */
public class Trick {

  private class PlayedCard {
    private Player player;
    private Card card;

    private PlayedCard(Player player, Card card) {
      this.player = player;
      this.card = card;
    }

    @Override
    public String toString() {
      return String.format("{%s:%s}", player, card);
    }
  }

  private Suit trumpSuit;
  private List<PlayedCard> playedCards;

  private Trick(Suit trumpSuit, List<PlayedCard> playedCards) {
    this.trumpSuit = trumpSuit;
    this.playedCards = playedCards;
  }

  public static Trick emptyTrick(Suit trumpSuit) {
    return new Trick(trumpSuit, new ArrayList<PlayedCard>());
  }

  /**
   * Add a card played by a player to the trick.
   *
   * @param player is the player who played the card.
   * @param card is the card played by the player.
   */
  public void add(Player player, Card card) {
    playedCards.add(new PlayedCard(player, card));
  }

  /**
   * Get the current master player of the trick, i.e. the player who has the current highest card in
   * the trick, i.e. the player who will win the trick if no higher card is played.
   *
   * <p><strong>NB:</strong> A runtime exception is thrown if the trick is empty.
   *
   * @return the trick's master.
   */
  public Player getMaster() {
    Suit desiredSuit = getDesiredSuit();
    ValuedCardComparator comparator = new ValuedCardComparator(desiredSuit);
    PlayedCard masterCard =
        playedCards.stream()
            .max(
                (pc1, pc2) ->
                    comparator.compare(
                        ValuedCard.fromCard(pc1.card, trumpSuit),
                        ValuedCard.fromCard(pc2.card, trumpSuit)))
            .orElse(null);
    if (masterCard == null) {
      throw new RuntimeException("Trick is empty; it has no master");
    }
    return masterCard.player;
  }

  /**
   * Get the desired suit of the trick, i.e. the suit of the first card played in the trick.
   *
   * <p><strong>NB:</strong> A runtime exception is thrown if the trick is empty.
   *
   * @return the trick's desired suit.
   */
  public Suit getDesiredSuit() {
    if (isEmpty()) {
      throw new RuntimeException("Trick is empty: it has no desired suit");
    }
    return playedCards.get(0).card.getSuit();
  }

  /**
   * Get the highest trump card that was played in the trick.
   *
   * @return the highest trump card played in the trick, null if no trump card was played.
   */
  public ValuedCard getHighestTrump() {
    ValuedCardComparator comparator = new ValuedCardComparator(trumpSuit);
    return playedCards.stream()
        .filter(pc -> pc.card.getSuit().equals(trumpSuit))
        .map(pc -> ValuedCard.fromCard(pc.card, true))
        .max(comparator)
        .orElse(null);
  }

  /**
   * Check if the trick is empty.
   *
   * @return a boolean indicating if there is any card in the trick.
   */
  public boolean isEmpty() {
    return playedCards.isEmpty();
  }

  @Override
  public String toString() {
    return playedCards.toString();
  }
}
