package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.cards.ValuedCard;
import com.coincoinche.engine.contracts.Contract;
import com.coincoinche.engine.contracts.ContractFactory;
import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/** Unit tests for the playing moves. */
public class MovePlayingTest extends GameEngineTestHelper {

  private CoincheGame initialGamePlayingPhase(
      int firstPlayerIndex, Contract highestBidding, int multiplier) {
    createTeams();
    Player firstPlayer = intToPlayer(firstPlayerIndex);
    createCoincheGame();
    GameStatePlaying state = GameStatePlaying.initialGameStatePlaying(firstPlayer, highestBidding);
    state.setTeams(List.of(t1, t2));
    CoincheGameRound round = coincheGame.getCurrentRound();
    round.setState(state);
    round.setCurrentPlayer(state.getCurrentPlayer());
    return coincheGame;
  }

  @Test
  public void applyOnGame() {
    /*
     * NEW TEST CASE
     */
    String testCaseName = "New phase - card not owned is illegal";
    Contract contract = ContractFactory.createContract(100, Suit.HEARTS);
    CoincheGame game = initialGamePlayingPhase(1, contract, 1);
    contract = contract.withPlayer(p1);
    assignCards(
        Map.of(
            1, List.of("Jh", "As", "Td", "Jc", "Ah", "8h", "Js", "9h"),
            2, List.of("Ks", "Jd", "8d", "7s", "Qd", "9s", "Ac", "Th"),
            3, List.of("Kc", "9c", "Tc", "Kh", "7d", "Ts", "Kd", "7h"),
            4, List.of("7c", "9d", "Qc", "Qs", "8c", "8s", "Ad", "Qh")));
    assertThatExceptionOfType(IllegalMoveException.class)
        .as(testCaseName)
        .isThrownBy(() -> MovePlaying.cardMove(stringToCard("Kh")).applyOnGame(game))
        .withNoCause();

    /*
     * NEW TEST CASE
     */
    testCaseName = "New phase - Jh(T)";
    CoincheGameRound preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Jh")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p1.getCards().contains(stringToCard("Jh")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    CoincheGameRound round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    GameState state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    GameStatePlaying playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 1", testCaseName))
        .isEqualTo(1);
    Trick currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is h", testCaseName))
        .isEqualTo(Suit.HEARTS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 20", testCaseName))
        .isEqualTo(20);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is Jh", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("Jh"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jh(T) - 7s is illegal";
    assertThatExceptionOfType(IllegalMoveException.class)
        .as(testCaseName)
        .isThrownBy(() -> MovePlaying.cardMove(stringToCard("7s")).applyOnGame(game))
        .withNoCause();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jh(T) - Th";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Th")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("Th")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 1", testCaseName))
        .isEqualTo(1);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is h", testCaseName))
        .isEqualTo(Suit.HEARTS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 30", testCaseName))
        .isEqualTo(30);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is Jh", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("Jh"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jh(T), Th - Kh";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Kh")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p3.getCards().contains(stringToCard("Kh")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 1", testCaseName))
        .isEqualTo(1);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is h", testCaseName))
        .isEqualTo(Suit.HEARTS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 34", testCaseName))
        .isEqualTo(34);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is Jh", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("Jh"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jh(T), Th, Kh - Qh";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Qh")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p4.getCards().contains(stringToCard("Qh")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 2", testCaseName))
        .isEqualTo(2);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick is empty", testCaseName))
        .isTrue();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jh is no longer legal";
    assertThatExceptionOfType(IllegalMoveException.class)
        .as(testCaseName)
        .isThrownBy(() -> MovePlaying.cardMove(stringToCard("Jh")).applyOnGame(game))
        .withNoCause();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jc";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Jc")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p1.getCards().contains(stringToCard("Jc")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 2", testCaseName))
        .isEqualTo(2);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is c", testCaseName))
        .isEqualTo(Suit.CLUBS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 2", testCaseName))
        .isEqualTo(2);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jc - Ac";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Ac")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("Ac")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 2", testCaseName))
        .isEqualTo(2);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is c", testCaseName))
        .isEqualTo(Suit.CLUBS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 13", testCaseName))
        .isEqualTo(13);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p2", testCaseName))
        .isEqualTo(p2);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jc, Ac - 9c";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("9c")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p3.getCards().contains(stringToCard("9c")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 2", testCaseName))
        .isEqualTo(2);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is c", testCaseName))
        .isEqualTo(Suit.CLUBS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 13", testCaseName))
        .isEqualTo(13);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p2", testCaseName))
        .isEqualTo(p2);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Jc, Ac, 9c - Qc";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Qc")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p4.getCards().contains(stringToCard("Qc")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 3", testCaseName))
        .isEqualTo(3);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick is empty", testCaseName))
        .isTrue();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Qd";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Qd")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("Qd")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 3", testCaseName))
        .isEqualTo(3);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is d", testCaseName))
        .isEqualTo(Suit.DIAMONDS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 3", testCaseName))
        .isEqualTo(3);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p2", testCaseName))
        .isEqualTo(p2);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Qd - Kd";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Kd")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p3.getCards().contains(stringToCard("Kd")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 3", testCaseName))
        .isEqualTo(3);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is d", testCaseName))
        .isEqualTo(Suit.DIAMONDS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 7", testCaseName))
        .isEqualTo(7);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p3", testCaseName))
        .isEqualTo(p3);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Qd, Kd - Ad";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Ad")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p4.getCards().contains(stringToCard("Ad")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 3", testCaseName))
        .isEqualTo(3);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is d", testCaseName))
        .isEqualTo(Suit.DIAMONDS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 18", testCaseName))
        .isEqualTo(18);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p4", testCaseName))
        .isEqualTo(p4);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Qd, Kd, Ad - Td";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Td")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p1.getCards().contains(stringToCard("Td")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 4", testCaseName))
        .isEqualTo(4);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick is empty", testCaseName))
        .isTrue();

    /*
     * NEW TEST CASE
     */
    testCaseName = "9d";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("9d")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p4.getCards().contains(stringToCard("9d")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 4", testCaseName))
        .isEqualTo(4);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is d", testCaseName))
        .isEqualTo(Suit.DIAMONDS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 0", testCaseName))
        .isEqualTo(0);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p4", testCaseName))
        .isEqualTo(p4);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "9d - 8h";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("8h")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p1.getCards().contains(stringToCard("8h")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 4", testCaseName))
        .isEqualTo(4);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is d", testCaseName))
        .isEqualTo(Suit.DIAMONDS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 0", testCaseName))
        .isEqualTo(0);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is 8h", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("8h"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "9d, 8h - 8d";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("8d")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("8d")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 4", testCaseName))
        .isEqualTo(4);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is d", testCaseName))
        .isEqualTo(Suit.DIAMONDS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 0", testCaseName))
        .isEqualTo(0);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is 8h", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("8h"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "9d, 8h, 8d - 7d";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("7d")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("7d")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 5", testCaseName))
        .isEqualTo(5);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick is empty", testCaseName))
        .isTrue();

    /*
     * NEW TEST CASE
     */
    testCaseName = "As";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("As")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p1.getCards().contains(stringToCard("As")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 5", testCaseName))
        .isEqualTo(5);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is s", testCaseName))
        .isEqualTo(Suit.SPADES);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 11", testCaseName))
        .isEqualTo(11);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "As - 7s";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("7s")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("7s")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 5", testCaseName))
        .isEqualTo(5);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is s", testCaseName))
        .isEqualTo(Suit.SPADES);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 11", testCaseName))
        .isEqualTo(11);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "As, 7s - Ts";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Ts")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p3.getCards().contains(stringToCard("Ts")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 37 pts (trick)", testCaseName))
        .isEqualTo(37);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 5", testCaseName))
        .isEqualTo(5);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is s", testCaseName))
        .isEqualTo(Suit.SPADES);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 0", testCaseName))
        .isEqualTo(21);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "As, 7s, Ts - 8s";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("8s")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p4.getCards().contains(stringToCard("8s")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 58 pts (trick)", testCaseName))
        .isEqualTo(58);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 6", testCaseName))
        .isEqualTo(6);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick is empty", testCaseName))
        .isTrue();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Js";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Js")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p1.getCards().contains(stringToCard("Js")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 58 pts (trick)", testCaseName))
        .isEqualTo(58);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 6", testCaseName))
        .isEqualTo(6);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is s", testCaseName))
        .isEqualTo(Suit.SPADES);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 2", testCaseName))
        .isEqualTo(2);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Js - Ks";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Ks")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("Ks")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 58 pts (trick)", testCaseName))
        .isEqualTo(58);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 6", testCaseName))
        .isEqualTo(6);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is s", testCaseName))
        .isEqualTo(Suit.SPADES);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 6", testCaseName))
        .isEqualTo(6);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p2", testCaseName))
        .isEqualTo(p2);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Js, Ks - 7h";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("7h")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p3.getCards().contains(stringToCard("7h")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 58 pts (trick)", testCaseName))
        .isEqualTo(58);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 0 pts (trick)", testCaseName))
        .isEqualTo(0);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 6", testCaseName))
        .isEqualTo(6);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is s", testCaseName))
        .isEqualTo(Suit.SPADES);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 6", testCaseName))
        .isEqualTo(6);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p3", testCaseName))
        .isEqualTo(p3);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is 7h", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("7h"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "Js, Ks, 7h - Qs";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Qs")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p4.getCards().contains(stringToCard("Qs")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 58 pts (trick)", testCaseName))
        .isEqualTo(58);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 9 pts (trick)", testCaseName))
        .isEqualTo(9);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 7", testCaseName))
        .isEqualTo(7);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick is empty", testCaseName))
        .isTrue();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Tc";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Tc")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p3.getCards().contains(stringToCard("Tc")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 58 pts (trick)", testCaseName))
        .isEqualTo(58);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 9 pts (trick)", testCaseName))
        .isEqualTo(9);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 7", testCaseName))
        .isEqualTo(7);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is c", testCaseName))
        .isEqualTo(Suit.CLUBS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 10", testCaseName))
        .isEqualTo(10);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p3", testCaseName))
        .isEqualTo(p3);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Tc - 7c";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("7c")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p4.getCards().contains(stringToCard("7c")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 58 pts (trick)", testCaseName))
        .isEqualTo(58);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 9 pts (trick)", testCaseName))
        .isEqualTo(9);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 7", testCaseName))
        .isEqualTo(7);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is c", testCaseName))
        .isEqualTo(Suit.CLUBS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 10", testCaseName))
        .isEqualTo(10);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p3", testCaseName))
        .isEqualTo(p3);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "Tc, 7c - Ah";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Ah")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p1.getCards().contains(stringToCard("Ah")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 58 pts (trick)", testCaseName))
        .isEqualTo(58);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 9 pts (trick)", testCaseName))
        .isEqualTo(9);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 7", testCaseName))
        .isEqualTo(7);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is c", testCaseName))
        .isEqualTo(Suit.CLUBS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 21", testCaseName))
        .isEqualTo(21);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is Ah", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("Ah"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "Tc, 7c, Ah - 9s";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("9s")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("9s")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 79 pts (trick)", testCaseName))
        .isEqualTo(79);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 9 pts (trick)", testCaseName))
        .isEqualTo(9);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 8", testCaseName))
        .isEqualTo(8);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick is empty", testCaseName))
        .isTrue();

    /*
     * NEW TEST CASE
     */
    testCaseName = "9h";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("9h")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p1.getCards().contains(stringToCard("9h")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 79 pts (trick)", testCaseName))
        .isEqualTo(79);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 9 pts (trick)", testCaseName))
        .isEqualTo(9);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 8", testCaseName))
        .isEqualTo(8);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is h", testCaseName))
        .isEqualTo(Suit.HEARTS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 14", testCaseName))
        .isEqualTo(14);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is 9h", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("9h"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "9h - Jd";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Jd")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p2.getCards().contains(stringToCard("Jd")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 79 pts (trick)", testCaseName))
        .isEqualTo(79);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 9 pts (trick)", testCaseName))
        .isEqualTo(9);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 8", testCaseName))
        .isEqualTo(8);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is h", testCaseName))
        .isEqualTo(Suit.HEARTS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 16", testCaseName))
        .isEqualTo(16);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is 9h", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("9h"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "9h, Jd - Kc";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("Kc")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    assertThat(p3.getCards().contains(stringToCard("Kc")))
        .as(String.format("%s: check card has disappeared from hand", testCaseName))
        .isFalse();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is playing state", testCaseName))
        .isInstanceOf(GameStatePlaying.class);
    playingState = (GameStatePlaying) state;
    assertThat(playingState.getTrickPointsForPlayer(p1))
        .as(String.format("%s: check p1 has 79 pts (trick)", testCaseName))
        .isEqualTo(79);
    assertThat(playingState.getTrickPointsForPlayer(p2))
        .as(String.format("%s: check p2 has 16 pts (trick)", testCaseName))
        .isEqualTo(16);
    assertThat(playingState.getTrickPointsForPlayer(p3))
        .as(String.format("%s: check p3 has 9 pts (trick)", testCaseName))
        .isEqualTo(9);
    assertThat(playingState.getTrickPointsForPlayer(p4))
        .as(String.format("%s: check p4 has 28 pts (trick)", testCaseName))
        .isEqualTo(28);
    assertThat(playingState.getCurrentTrickNumber())
        .as(String.format("%s: check current trick # is 8", testCaseName))
        .isEqualTo(8);
    currentTrick = playingState.getCurrentTrick();
    assertThat(currentTrick.isEmpty())
        .as(String.format("%s: check current trick isn't empty", testCaseName))
        .isFalse();
    assertThat(currentTrick.getDesiredSuit())
        .as(String.format("%s: checktrick's desired suit is h", testCaseName))
        .isEqualTo(Suit.HEARTS);
    assertThat(currentTrick.getValue())
        .as(String.format("%s: check trick's value is 20", testCaseName))
        .isEqualTo(20);
    assertThat(currentTrick.getMaster())
        .as(String.format("%s: check trick's master is p1", testCaseName))
        .isEqualTo(p1);
    assertThat(currentTrick.getHighestTrump())
        .as(String.format("%s: check trick's highest trump is 9h", testCaseName))
        .isEqualTo(ValuedCard.fromCard(stringToCard("9h"), Suit.HEARTS));

    /*
     * NEW TEST CASE
     */
    testCaseName = "9h, Jd, Kc - 8c";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MovePlaying.cardMove(stringToCard("8c")).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round has changed", testCaseName))
        .isNotEqualTo(preMoveRound);
    assertThat(game.getRedTeam().getPoints())
        .as(String.format("%s: check red team's points have been updated", testCaseName))
        .isEqualTo(218);
    assertThat(game.getBlueTeam().getPoints())
        .as(String.format("%s: check blue team's points have been updated", testCaseName))
        .isEqualTo(44);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    GameStateBidding biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is not coinched", testCaseName))
        .isFalse();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    Contract highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is null", testCaseName))
        .isNull();
  }

  @Test
  public void compareTo() {

    class TestCase extends GameEngineTestHelper.ComparisonTestCase<MovePlaying> {
      int expected;

      TestCase(String name, MovePlaying m1, MovePlaying m2, int expected) {
        // super.expected is not used
        super(name, m1, m2, true);
        this.expected = expected;
      }

      @Override
      protected void runAssertions() {
        if (expected > 0) {
          assertThat(o1).as("Check greater than").isGreaterThan(o2);
          return;
        }
        if (expected < 0) {
          assertThat(o1).as("Check less than").isLessThan(o2);
          return;
        }
        assertThat(o1).as("Check equals").isEqualByComparingTo(o2);
      }
    }

    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase(
                    "As - As",
                    MovePlaying.cardMove(stringToCard("As")),
                    MovePlaying.cardMove(stringToCard("As")),
                    0),
                new TestCase(
                    "As - Js",
                    MovePlaying.cardMove(stringToCard("As")),
                    MovePlaying.cardMove(stringToCard("Js")),
                    1),
                new TestCase(
                    "Ts - 9s",
                    MovePlaying.cardMove(stringToCard("Ts")),
                    MovePlaying.cardMove(stringToCard("9s")),
                    1),
                new TestCase(
                    "As - Ah",
                    MovePlaying.cardMove(stringToCard("As")),
                    MovePlaying.cardMove(stringToCard("Ah")),
                    -1),
                new TestCase(
                    "7c - Tc",
                    MovePlaying.cardMove(stringToCard("7c")),
                    MovePlaying.cardMove(stringToCard("Tc")),
                    -1),
                new TestCase(
                    "7c - Ad",
                    MovePlaying.cardMove(stringToCard("7c")),
                    MovePlaying.cardMove(stringToCard("Ad")),
                    -1),
                new TestCase(
                    "Tc - Qc",
                    MovePlaying.cardMove(stringToCard("Tc")),
                    MovePlaying.cardMove(stringToCard("Qc")),
                    -1),
                new TestCase(
                    "Jh - Jd",
                    MovePlaying.cardMove(stringToCard("Jh")),
                    MovePlaying.cardMove(stringToCard("Jd")),
                    -1),
                new TestCase(
                    "8c - 8c",
                    MovePlaying.cardMove(stringToCard("8c")),
                    MovePlaying.cardMove(stringToCard("8c")),
                    0),
                new TestCase(
                    "Jd - Qd",
                    MovePlaying.cardMove(stringToCard("Jd")),
                    MovePlaying.cardMove(stringToCard("Qd")),
                    -1),
                new TestCase(
                    "Td - Qd",
                    MovePlaying.cardMove(stringToCard("Td")),
                    MovePlaying.cardMove(stringToCard("Qd")),
                    -1),
                new TestCase(
                    "Ad - Qd",
                    MovePlaying.cardMove(stringToCard("Ad")),
                    MovePlaying.cardMove(stringToCard("Qd")),
                    1),
                new TestCase(
                    "9s - 9c",
                    MovePlaying.cardMove(stringToCard("9s")),
                    MovePlaying.cardMove(stringToCard("9c")),
                    -1)));
    testCases.forEach(TestCase::run);
  }

  @Test
  public void equals() {

    class TestCase extends GameEngineTestHelper.ComparisonTestCase<MovePlaying> {
      TestCase(String name, MovePlaying m1, MovePlaying m2, boolean expected) {
        super(name, m1, m2, expected);
      }

      @Override
      protected void runAssertions() {
        assertThat(o1.equals(o2)).as("Check move equality").isEqualTo(expected);
      }
    }
    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase(
                    "As - As",
                    MovePlaying.cardMove(stringToCard("As")),
                    MovePlaying.cardMove(stringToCard("As")),
                    true),
                new TestCase(
                    "As - Js",
                    MovePlaying.cardMove(stringToCard("As")),
                    MovePlaying.cardMove(stringToCard("Js")),
                    false),
                new TestCase(
                    "Ts - 9s",
                    MovePlaying.cardMove(stringToCard("Ts")),
                    MovePlaying.cardMove(stringToCard("9s")),
                    false),
                new TestCase(
                    "As - Ah",
                    MovePlaying.cardMove(stringToCard("As")),
                    MovePlaying.cardMove(stringToCard("Ah")),
                    false),
                new TestCase(
                    "7c - Tc",
                    MovePlaying.cardMove(stringToCard("7c")),
                    MovePlaying.cardMove(stringToCard("Tc")),
                    false),
                new TestCase(
                    "7c - Ad",
                    MovePlaying.cardMove(stringToCard("7c")),
                    MovePlaying.cardMove(stringToCard("Ad")),
                    false),
                new TestCase(
                    "Qc - Qc",
                    MovePlaying.cardMove(stringToCard("Qc")),
                    MovePlaying.cardMove(stringToCard("Qc")),
                    true),
                new TestCase(
                    "Jh - Jd",
                    MovePlaying.cardMove(stringToCard("Jh")),
                    MovePlaying.cardMove(stringToCard("Jd")),
                    false),
                new TestCase(
                    "8c - 8c",
                    MovePlaying.cardMove(stringToCard("8c")),
                    MovePlaying.cardMove(stringToCard("8c")),
                    true),
                new TestCase(
                    "Jd - Qd",
                    MovePlaying.cardMove(stringToCard("Jd")),
                    MovePlaying.cardMove(stringToCard("Qd")),
                    false),
                new TestCase(
                    "Td - Qd",
                    MovePlaying.cardMove(stringToCard("Td")),
                    MovePlaying.cardMove(stringToCard("Qd")),
                    false),
                new TestCase(
                    "Ad - Qd",
                    MovePlaying.cardMove(stringToCard("Ad")),
                    MovePlaying.cardMove(stringToCard("Ad")),
                    true),
                new TestCase(
                    "9s - 9c",
                    MovePlaying.cardMove(stringToCard("9s")),
                    MovePlaying.cardMove(stringToCard("9c")),
                    false)));
    testCases.forEach(TestCase::run);
  }
}
