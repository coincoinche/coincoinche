package com.coincoinche.engine;

import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.cards.ValuedCard;
import com.coincoinche.engine.cards.ValuedCardComparator;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** TODO nockty add detailed documentation here. */
public class GameStatePlaying implements GameStateTerminal {
  private Player lastTrickMaster;
  private Player currentPlayer;
  private Suit trumpSuit;
  private Trick currentTrick;

  GameStatePlaying(Player currentPlayer, Suit trumpSuit, Trick currentTrick) {
    this.currentPlayer = currentPlayer;
    this.trumpSuit = trumpSuit;
    this.currentTrick = currentTrick;
  }

  /**
   * Create a new initial game state for the playing phase.
   * @param firstPlayer is the first player of the playing phase.
   * @param trumpSuit is the trump suit for the playing phase.
   * @return the newly created state.
   */
  public static GameStatePlaying initialGameStatePlaying(Player firstPlayer, Suit trumpSuit) {
    Trick currentTrick = Trick.emptyTrick(trumpSuit);
    // start state with trick #1
    return new GameStatePlaying(firstPlayer, trumpSuit, currentTrick, 1);
  }

  @Override
  public List<Move> getLegalMoves() {
    return getLegalCardsStream()
        .map(MovePlaying::cardMove)
        .sorted()
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private Stream<Card> getLegalCardsStream() {
    // Initialize legal cards with the current player's cards
    Supplier<Stream<Card>> allCardsSupplier = () -> currentPlayer.getCards().stream();
    // first player can play anything
    if (currentTrick.isEmpty()) {
      return allCardsSupplier.get();
    }
    Suit desiredSuit = currentTrick.getDesiredSuit();
    // apply a first filter on cards
    Supplier<Stream<Card>> filteredCardsSupplier =
        () ->
            defaultIfEmpty(
                // must play desired suit if possible
                allCardsSupplier.get().filter(card -> card.getSuit().equals(desiredSuit)),
                // otherwise, must cut with trump if possible (except if team mate is master)
                allCardsSupplier
                    .get()
                    .filter(
                        card -> {
                          if (currentTrick.getMaster().isTeamMate(currentPlayer)) {
                            return true;
                          }
                          return card.getSuit().equals(trumpSuit);
                        }));
    // generate legal trumps from filtered cards
    Supplier<Stream<Card>> legalTrumpsSupplier =
        () ->
            defaultIfEmpty(
                // trump must be higher than current highest trump if possible
                filteredCardsSupplier
                    .get()
                    .filter(card -> card.getSuit().equals(trumpSuit))
                    .filter(
                        card -> {
                          ValuedCard highestTrump = currentTrick.getHighestTrump();
                          if (highestTrump == null) {
                            return true;
                          }
                          ValuedCardComparator trumpComparator =
                              new ValuedCardComparator(trumpSuit);
                          return trumpComparator.compare(
                                  ValuedCard.fromCard(card, true), highestTrump)
                              >= 0;
                        }),
                // if no trump is higher, fall back to all the trumps
                filteredCardsSupplier.get().filter(card -> card.getSuit().equals(trumpSuit)));
    return defaultIfEmpty(
        // filter cards again by removing illegal trumps
        filteredCardsSupplier
            .get()
            .filter(
                card ->
                    !card.getSuit().equals(trumpSuit)
                        || legalTrumpsSupplier.get().anyMatch(t -> card.equals(t))),
        // if there is no such card, fall back to any card
        allCardsSupplier.get());
  }

  /**
   * Get a stream with a fall back if it is empty.
   *
   * @param <T> is the type contained in the stream.
   * @param stream is the stream that must not be empty.
   * @param defaultStream is the fallback stream if the first one is empty.
   * @return first stream is not empty, else fallback stream.
   */
  private static <T> Stream<T> defaultIfEmpty(Stream<T> stream, Stream<T> defaultStream) {
    return StreamSupport.stream(
        new Spliterator<T>() {
          Spliterator<T> it = stream.spliterator();
          boolean seen;

          @Override
          public boolean tryAdvance(Consumer<? super T> action) {
            boolean r = it.tryAdvance(action);
            // if there is no first element, fall back to the default stream
            if (!seen && !r) {
              it = defaultStream.spliterator();
              r = it.tryAdvance(action);
            }
            seen = true;
            return r;
          }

          @Override
          public Spliterator<T> trySplit() {
            // no support for splitting
            return null;
          }

          @Override
          public long estimateSize() {
            return it.estimateSize();
          }

          @Override
          public int characteristics() {
            return it.characteristics();
          }
        },
        false);
  }

  // TODO nockty see if we can use a default method here
  @Override
  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  @Override
  public boolean mustChange() {
    // TODO Auto-generated method stub
    return false;
  }

  // TODO nockty see if we can use a default method here
  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public Team getWinnerTeam() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getWinnerPoints() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void rotatePlayers(CoincheGameRound round) {
    if (lastTrickMaster != null) {
      currentPlayer = lastTrickMaster;
    }
    round.setCurrentPlayer(currentPlayer);
  }
}
