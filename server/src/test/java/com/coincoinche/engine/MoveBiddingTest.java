package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.teams.Team;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** Unit tests for the bidding moves. */
public class MoveBiddingTest extends GameEngineTestHelper {

  private CoincheGame simpleCoincheGame() {
    createTeams();
    createCoincheGame();
    return coincheGame;
  }

  @Test
  public void applyOnGame() {
    /*
     * NEW TEST CASE
     */
    String testCaseName = "New game - coinche is illegal";
    assertThatExceptionOfType(IllegalMoveException.class)
        .as(testCaseName)
        .isThrownBy(() -> MoveBidding.coincheMove().applyOnGame(simpleCoincheGame()))
        .withNoCause();

    /*
     * NEW TEST CASE
     */
    testCaseName = "New game - surcoinche is illegal";
    assertThatExceptionOfType(IllegalMoveException.class)
        .as(testCaseName)
        .isThrownBy(() -> MoveBidding.surcoincheMove().applyOnGame(simpleCoincheGame()))
        .withNoCause();

    /*
     * NEW TEST CASE
     */
    testCaseName = "New game - 100 HEARTS";
    CoincheGame game = simpleCoincheGame();
    CoincheGameRound preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result =
          MoveBidding.contractMove(Contract.pointsContract(100, Suit.HEARTS)).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    CoincheGameRound round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    GameState state = round.getState();
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
        .as(String.format("%s: check highest bidding is 100 HEARTS", testCaseName))
        .isEqualTo(Contract.pointsContract(100, Suit.HEARTS));
    assertThat(highestBidding.getPlayer())
        .as(String.format("%s: check highest bidding belongs to player 1", testCaseName))
        .isEqualTo(p1);

    /*
     * NEW TEST CASE
     */
    testCaseName = "100 HEARTS - coinche";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MoveBidding.coincheMove().applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is coinched", testCaseName))
        .isTrue();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is 100 HEARTS", testCaseName))
        .isEqualTo(Contract.pointsContract(100, Suit.HEARTS));
    assertThat(highestBidding.getPlayer())
        .as(String.format("%s: check highest bidding belongs to player 1", testCaseName))
        .isEqualTo(p1);

    /*
     * NEW TEST CASE
     */
    testCaseName = "100 HEARTS, coinche - pass";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MoveBidding.passMove().applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is coinched", testCaseName))
        .isTrue();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is 100 HEARTS", testCaseName))
        .isEqualTo(Contract.pointsContract(100, Suit.HEARTS));
    assertThat(highestBidding.getPlayer())
        .as(String.format("%s: check highest bidding belongs to player 1", testCaseName))
        .isEqualTo(p1);

    /*
     * NEW TEST CASE
     */
    testCaseName = "100 HEARTS, coinche, pass - pass";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MoveBidding.passMove().applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is coinched", testCaseName))
        .isTrue();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is 100 HEARTS", testCaseName))
        .isEqualTo(Contract.pointsContract(100, Suit.HEARTS));
    assertThat(highestBidding.getPlayer())
        .as(String.format("%s: check highest bidding belongs to player 1", testCaseName))
        .isEqualTo(p1);

    /*
     * NEW TEST CASE
     */
    testCaseName = "100 HEARTS, coinche, pass, pass - surcoinche";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MoveBidding.surcoincheMove().applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    // TODO nockty: this test will need to change when we add a new phase to the game
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round has changed", testCaseName))
        .isNotEqualTo(preMoveRound);
    assertThat(game.getRedTeam().getPoints())
        .as(String.format("%s: check red team's points have been updated", testCaseName))
        .isEqualTo(100);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is not coinched", testCaseName))
        .isFalse();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "80 HEARTS - 90 SPADES";
    game = simpleCoincheGame();
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result =
          MoveBidding.contractMove(Contract.pointsContract(80, Suit.HEARTS)).applyOnGame(game);
      MoveBidding.contractMove(Contract.pointsContract(90, Suit.SPADES)).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 3", testCaseName))
        .isEqualTo(p3);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is not coinched", testCaseName))
        .isFalse();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is 90 SPADES", testCaseName))
        .isEqualTo(Contract.pointsContract(90, Suit.SPADES));
    assertThat(highestBidding.getPlayer())
        .as(String.format("%s: check highest bidding belongs to player 2", testCaseName))
        .isEqualTo(p2);

    /*
     * NEW TEST CASE
     */
    testCaseName = "80H, 90S - 100 HEARTS";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result =
          MoveBidding.contractMove(Contract.pointsContract(100, Suit.HEARTS)).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 4", testCaseName))
        .isEqualTo(p4);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is not coinched", testCaseName))
        .isFalse();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is 100 HEARTS", testCaseName))
        .isEqualTo(Contract.pointsContract(100, Suit.HEARTS));
    assertThat(highestBidding.getPlayer())
        .as(String.format("%s: check highest bidding belongs to player 3", testCaseName))
        .isEqualTo(p3);

    /*
     * NEW TEST CASE
     */
    testCaseName = "80H, 90S, 100H - CAPOT CLUBS";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result =
          MoveBidding.contractMove(Contract.capotContract(Suit.CLUBS)).applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 1", testCaseName))
        .isEqualTo(p1);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is not coinched", testCaseName))
        .isFalse();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is CAPOT CLUBS", testCaseName))
        .isEqualTo(Contract.capotContract(Suit.CLUBS));
    assertThat(highestBidding.getPlayer())
        .as(String.format("%s: check highest bidding belongs to player 4", testCaseName))
        .isEqualTo(p4);

    /*
     * NEW TEST CASE
     */
    testCaseName = "80H, 90S, 100H, CAPC - COINCHE";
    preMoveRound = game.getCurrentRound();
    try {
      GameResult<Team> result = MoveBidding.coincheMove().applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round hasn't changed", testCaseName))
        .isEqualTo(preMoveRound);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is coinched", testCaseName))
        .isTrue();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is CAPOT CLUBS", testCaseName))
        .isEqualTo(Contract.capotContract(Suit.CLUBS));
    assertThat(highestBidding.getPlayer())
        .as(String.format("%s: check highest bidding belongs to player 4", testCaseName))
        .isEqualTo(p4);

    /*
     * NEW TEST CASE
     */
    testCaseName = "80H, 90S, 100H, CAPC, COINCHE - 3x PASS";
    preMoveRound = game.getCurrentRound();
    try {
      MoveBidding.passMove().applyOnGame(game);
      MoveBidding.passMove().applyOnGame(game);
      GameResult<Team> result = MoveBidding.passMove().applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    // TODO nockty: this test will need to change when we add a new phase to the game
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round has changed", testCaseName))
        .isNotEqualTo(preMoveRound);
    assertThat(game.getBlueTeam().getPoints())
        .as(String.format("%s: check blue team's points have been updated", testCaseName))
        .isEqualTo(250);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is not coinched", testCaseName))
        .isFalse();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is null", testCaseName))
        .isNull();

    /*
     * NEW TEST CASE
     */
    testCaseName = "NEW GAME - 4x PASS";
    game = simpleCoincheGame();
    preMoveRound = game.getCurrentRound();
    try {
      MoveBidding.passMove().applyOnGame(game);
      MoveBidding.passMove().applyOnGame(game);
      MoveBidding.passMove().applyOnGame(game);
      GameResult<Team> result = MoveBidding.passMove().applyOnGame(game);
      assertThat(result.isFinished())
          .as(String.format("%s: check game hasn't finished", testCaseName))
          .isFalse();
    } catch (IllegalMoveException e) {
      throw new RuntimeException(String.format("%s: Illegal move wasn't expected", testCaseName));
    }
    round = game.getCurrentRound();
    assertThat(round)
        .as(String.format("%s: check round has changed", testCaseName))
        .isNotEqualTo(preMoveRound);
    assertThat(game.getRedTeam().getPoints())
        .as(String.format("%s: check red team's points haven't changed", testCaseName))
        .isEqualTo(0);
    assertThat(game.getBlueTeam().getPoints())
        .as(String.format("%s: check blue team's points haven't changed", testCaseName))
        .isEqualTo(0);
    assertThat(round.getCurrentPlayer())
        .as(String.format("%s: check current player index is 2", testCaseName))
        .isEqualTo(p2);
    state = round.getState();
    assertThat(state)
        .as(String.format("%s: check state is bidding state", testCaseName))
        .isInstanceOf(GameStateBidding.class);
    biddingState = (GameStateBidding) state;
    assertThat(biddingState.isCoinched())
        .as(String.format("%s: check is not coinched", testCaseName))
        .isFalse();
    assertThat(biddingState.isSurcoinched())
        .as(String.format("%s: check is not surcoinched", testCaseName))
        .isFalse();
    highestBidding = biddingState.getHighestBidding();
    assertThat(highestBidding)
        .as(String.format("%s: check highest bidding is null", testCaseName))
        .isNull();
  }

  @Test
  public void compareTo() {

    class TestCase extends GameEngineTestHelper.ComparisonTestCase<MoveBidding> {
      int expected;

      TestCase(String name, MoveBidding m1, MoveBidding m2, int expected) {
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
                new TestCase("pass - pass", MoveBidding.passMove(), MoveBidding.passMove(), 0),
                new TestCase(
                    "coinche - coinche", MoveBidding.coincheMove(), MoveBidding.coincheMove(), 0),
                new TestCase(
                    "surcoinche - surcoinche",
                    MoveBidding.surcoincheMove(),
                    MoveBidding.surcoincheMove(),
                    0),
                new TestCase(
                    "pass - coinche", MoveBidding.passMove(), MoveBidding.coincheMove(), -1),
                new TestCase(
                    "surcoinche - coinche",
                    MoveBidding.surcoincheMove(),
                    MoveBidding.coincheMove(),
                    1),
                new TestCase(
                    "surcoinche - pass", MoveBidding.surcoincheMove(), MoveBidding.passMove(), 1),
                new TestCase(
                    "pass - capot heart",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    -1),
                new TestCase(
                    "pass - 130 spades",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    -1),
                new TestCase(
                    "coinche - capot heart",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    -1),
                new TestCase(
                    "130 spades - pass",
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    MoveBidding.passMove(),
                    1),
                new TestCase(
                    "130 spades - capot heart",
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    -1),
                new TestCase(
                    "130 heart - 130 spades",
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    1),
                new TestCase(
                    "capot heart - capot heart",
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    0),
                new TestCase(
                    "generale clubs - generale clubs",
                    MoveBidding.contractMove(Contract.generaleContract(Suit.CLUBS)),
                    MoveBidding.contractMove(Contract.generaleContract(Suit.CLUBS)),
                    0),
                new TestCase(
                    "capot clubs - capot spades",
                    MoveBidding.contractMove(Contract.capotContract(Suit.CLUBS)),
                    MoveBidding.contractMove(Contract.capotContract(Suit.SPADES)),
                    1),
                new TestCase(
                    "capot heart - generale spades",
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.generaleContract(Suit.SPADES)),
                    -1),
                new TestCase(
                    "generale heart - generale diamonds",
                    MoveBidding.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.generaleContract(Suit.DIAMONDS)),
                    -1),
                new TestCase(
                    "90 heart - 90 heart",
                    MoveBidding.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    0),
                new TestCase(
                    "90 heart - 130 heart",
                    MoveBidding.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                    -1),
                new TestCase(
                    "110 diamonds - 110 spades",
                    MoveBidding.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
                    MoveBidding.contractMove(Contract.pointsContract(110, Suit.SPADES)),
                    1),
                new TestCase(
                    "capot heart - 100 heart",
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(100, Suit.HEARTS)),
                    1),
                new TestCase(
                    "generale heart - 140 diamonds",
                    MoveBidding.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(140, Suit.DIAMONDS)),
                    1)));
    testCases.forEach(TestCase::run);
  }

  @Test
  public void equals() {

    class TestCase extends GameEngineTestHelper.ComparisonTestCase<MoveBidding> {

      TestCase(String name, MoveBidding m1, MoveBidding m2, boolean expected) {
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
                new TestCase("pass - pass", MoveBidding.passMove(), MoveBidding.passMove(), true),
                new TestCase(
                    "coinche - coinche",
                    MoveBidding.coincheMove(),
                    MoveBidding.coincheMove(),
                    true),
                new TestCase(
                    "surcoinche - surcoinche",
                    MoveBidding.surcoincheMove(),
                    MoveBidding.surcoincheMove(),
                    true),
                new TestCase(
                    "pass - coinche", MoveBidding.passMove(), MoveBidding.coincheMove(), false),
                new TestCase(
                    "surcoinche - coinche",
                    MoveBidding.surcoincheMove(),
                    MoveBidding.coincheMove(),
                    false),
                new TestCase(
                    "surcoinche - pass",
                    MoveBidding.surcoincheMove(),
                    MoveBidding.passMove(),
                    false),
                new TestCase(
                    "pass - capot heart",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    false),
                new TestCase(
                    "pass - 130 spades",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    false),
                new TestCase(
                    "coinche - capot heart",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    false),
                new TestCase(
                    "coinche - 130 spades",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    false),
                new TestCase(
                    "surcoinche - capot heart",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    false),
                new TestCase(
                    "surcoinche - 130 spades",
                    MoveBidding.passMove(),
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    false),
                new TestCase(
                    "capot heart - capot heart",
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    true),
                new TestCase(
                    "generale clubs - generale clubs",
                    MoveBidding.contractMove(Contract.generaleContract(Suit.CLUBS)),
                    MoveBidding.contractMove(Contract.generaleContract(Suit.CLUBS)),
                    true),
                new TestCase(
                    "capot heart - capot spades",
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.capotContract(Suit.SPADES)),
                    false),
                new TestCase(
                    "capot heart - generale heart",
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    false),
                new TestCase(
                    "generale heart - generale diamonds",
                    MoveBidding.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.generaleContract(Suit.DIAMONDS)),
                    false),
                new TestCase(
                    "90 heart - 90 heart",
                    MoveBidding.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    true),
                new TestCase(
                    "90 heart - 130 heart",
                    MoveBidding.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                    false),
                new TestCase(
                    "110 diamonds - 110 spades",
                    MoveBidding.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
                    MoveBidding.contractMove(Contract.pointsContract(110, Suit.SPADES)),
                    false),
                new TestCase(
                    "capot heart - 100 heart",
                    MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(100, Suit.HEARTS)),
                    false),
                new TestCase(
                    "generale heart - 140 heart",
                    MoveBidding.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    MoveBidding.contractMove(Contract.pointsContract(140, Suit.HEARTS)),
                    false)));
    testCases.forEach(TestCase::run);
  }
}
