package com.coincoinche.engine;

import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.cards.Trick;
import com.coincoinche.engine.cards.ValuedCard;
import com.coincoinche.engine.cards.ValuedCardComparator;
import com.coincoinche.engine.contracts.Contract;
import com.coincoinche.engine.serialization.json.GameStatePlayingSerializer;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** TODO nockty add detailed documentation here. */
@JsonSerialize(using = GameStatePlayingSerializer.class)
public class GameStatePlaying implements GameStateTerminal {
  private static final int MAX_TRICK_NUMBER = 8;
  private Trick currentTrick;
  private Player lastTrickMaster;
  private Contract contract;
  private Player currentPlayer;
  private Map<Player, Integer> playerPoints = new HashMap<>();
  private List<Team> teams;
  private int multiplier = 1;
  private List<Trick> trickHistory = new ArrayList<>();

  protected GameStatePlaying(Player currentPlayer, Contract contract, Trick currentTrick) {
    this.currentPlayer = currentPlayer;
    this.contract = contract;
    this.currentTrick = currentTrick;
  }

  /**
   * Create a new initial game state for the playing phase.
   *
   * @param firstPlayer is the first player of the playing phase.
   * @param contract is the contract that must be fulfilled.
   * @return the newly created state.
   */
  public static GameStatePlaying initialGameStatePlaying(Player firstPlayer, Contract contract) {
    Trick currentTrick = Trick.emptyTrick(contract.getSuit());
    // start state with trick #1
    return new GameStatePlaying(firstPlayer, contract, currentTrick);
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
                          return card.getSuit().equals(getTrumpSuit());
                        }));
    // generate legal trumps from filtered cards
    Supplier<Stream<Card>> legalTrumpsSupplier =
        () ->
            defaultIfEmpty(
                // trump must be higher than current highest trump if possible
                filteredCardsSupplier
                    .get()
                    .filter(card -> card.getSuit().equals(getTrumpSuit()))
                    .filter(
                        card -> {
                          ValuedCard highestTrump = currentTrick.getHighestTrump();
                          if (highestTrump == null) {
                            return true;
                          }
                          ValuedCardComparator trumpComparator =
                              new ValuedCardComparator(getTrumpSuit());
                          return trumpComparator.compare(
                                  ValuedCard.fromCard(card, true), highestTrump)
                              >= 0;
                        }),
                // if no trump is higher, fall back to all the trumps
                filteredCardsSupplier.get().filter(card -> card.getSuit().equals(getTrumpSuit())));
    return defaultIfEmpty(
        // filter cards again by removing illegal trumps
        filteredCardsSupplier
            .get()
            .filter(
                card ->
                    !card.getSuit().equals(getTrumpSuit())
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

  /**
   * Close the current trick of the state:
   *
   * <ul>
   *   <li>Add trick's points to trick's master
   *   <li>Update state so that master is the next first player
   *   <li>Create a new empty trick for the state
   * </ul>
   *
   * <strong>NB:</strong> <i>A priori</i>, this method only makes sense for a complete trick.
   */
  protected void closeTrick() {
    // add points to master
    Player master = currentTrick.getMaster();
    int points = currentTrick.getValue();
    if (getCurrentTrickNumber() == 8) {
      // 10 de der
      points += 10;
    }
    playerPoints.put(master, points + playerPoints.getOrDefault(master, 0));
    // update last master
    lastTrickMaster = master;
    // clear trick
    trickHistory.add(currentTrick);
    currentTrick = Trick.emptyTrick(getTrumpSuit());
  }

  @Override
  public boolean mustChange() {
    return getCurrentTrickNumber() > MAX_TRICK_NUMBER;
  }

  protected void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  protected void setMultiplier(int multiplier) {
    this.multiplier = multiplier;
  }

  // TODO nockty see if we can use a default method here
  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Suit getTrumpSuit() {
    return contract.getSuit();
  }

  public int getCurrentTrickNumber() {
    return trickHistory.size() + 1;
  }

  public Trick getCurrentTrick() {
    return currentTrick;
  }

  public List<Trick> getTrickHistory() {
    return trickHistory;
  }

  public Contract getContract() {
    return contract;
  }

  public int getMultiplier() {
    return multiplier;
  }

  /**
   * Get points earned by the player in the current state.
   *
   * @param player is a player playing the game.
   * @return the points earned by that player during this state's tricks.
   */
  public int getTrickPointsForPlayer(Player player) {
    return playerPoints.getOrDefault(player, 0);
  }

  private int getTrickPointsForTeam(Team team) {
    int points = 0;
    for (Entry<Player, Integer> entry : playerPoints.entrySet()) {
      if (!entry.getKey().getTeam().equals(team)) {
        continue;
      }
      points += entry.getValue();
    }
    return points;
  }

  private Team getAttackTeam() {
    return contract.getPlayer().getTeam();
  }

  private Team getDefenseTeam() {
    int attackIndex = teams.indexOf(getAttackTeam());
    return teams.get((attackIndex + 1) % 2);
  }

  @Override
  public Map<Team, Integer> getTeamsPoints() {
    Map<Team, Integer> teamsPoints = new HashMap<>();
    Team attackTeam = getAttackTeam();
    Team defenseTeam = getDefenseTeam();
    if (contract.isSuccessful(this)) {
      teamsPoints.put(
          attackTeam,
          (getTrickPointsForTeam(attackTeam) + contract.getEarnedPoints()) * multiplier);
      teamsPoints.put(defenseTeam, getTrickPointsForTeam(defenseTeam));
      return teamsPoints;
    }
    teamsPoints.put(attackTeam, 0);
    teamsPoints.put(
        defenseTeam, (CoincheGame.getMaxTricksPoints() + contract.getEarnedPoints()) * multiplier);
    return teamsPoints;
  }

  @Override
  public void rotatePlayers(CoincheGameRound round) {
    // if current trick is empty, it means the previous one was complete
    if (!currentTrick.isEmpty()) {
      round.rotatePlayers();
      currentPlayer = round.getCurrentPlayer();
      return;
    }
    if (lastTrickMaster != null) {
      currentPlayer = lastTrickMaster;
    }
    round.setCurrentPlayer(currentPlayer);
  }
}
