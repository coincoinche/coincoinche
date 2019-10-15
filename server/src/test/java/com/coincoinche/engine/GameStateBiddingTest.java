package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.cards.Suit;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/** Unit tests for the bidding game. */
public class GameStateBiddingTest extends GameEngineTestHelper {

  private class TestCase extends GameEngineTestHelper.TestCase {
    private GameStateBidding input;
    private List<MoveBidding> expected;

    TestCase(String name, GameStateBidding input, List<MoveBidding> expected) {
      super(name);
      this.input = input;
      this.expected = expected;
    }

    @Override
    protected void runAssertions() {
      List<Move> actual = input.getLegalMoves();
      int expectedSize = expected.size();
      int actualSize = actual.size();
      assertThat(actualSize).as("Check sizes").isEqualTo(expectedSize);

      for (int i = 0; i < expectedSize; i++) {
        assertThat(actual.get(i)).as("Check moves").isEqualTo(expected.get(i));
      }
    }
  }

  @Before
  public void setUp() {
    createTeams();
  }

  @Test
  public void getLegalMoves() {
    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase(
                    "No Contract",
                    new GameStateBidding(p1, null, null, false, false),
                    new ArrayList<>(
                        List.of(
                            MoveBidding.passMove(),
                            MoveBidding.contractMove(Contract.pointsContract(80, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(80, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(80, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(80, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(90, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(90, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(90, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(100, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(100, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(100, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(100, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.SPADES)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.SPADES)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.DIAMONDS))))),
                new TestCase(
                    "Team mate has contract - not coinched",
                    new GameStateBidding(
                        p1,
                        null,
                        Contract.pointsContract(100, Suit.CLUBS).withPlayer(p3),
                        false,
                        false),
                    new ArrayList<>(
                        List.of(
                            MoveBidding.passMove(),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.SPADES)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.SPADES)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.DIAMONDS))))),
                new TestCase(
                    "Team mate has contract - coinched",
                    new GameStateBidding(
                        p1,
                        null,
                        Contract.pointsContract(90, Suit.DIAMONDS).withPlayer(p3),
                        true,
                        false),
                    new ArrayList<>(List.of(MoveBidding.passMove(), MoveBidding.surcoincheMove()))),
                new TestCase(
                    "Player has contract - coinched",
                    new GameStateBidding(
                        p1, null, Contract.capotContract(Suit.HEARTS).withPlayer(p1), true, false),
                    new ArrayList<>(List.of(MoveBidding.passMove(), MoveBidding.surcoincheMove()))),
                new TestCase(
                    "Opponent has contract - not coinched",
                    new GameStateBidding(
                        p2,
                        null,
                        Contract.pointsContract(80, Suit.HEARTS).withPlayer(p1),
                        false,
                        false),
                    new ArrayList<>(
                        List.of(
                            MoveBidding.passMove(),
                            MoveBidding.coincheMove(),
                            MoveBidding.contractMove(Contract.pointsContract(90, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(90, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(90, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(100, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(100, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(100, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(100, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(120, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(130, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(140, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(150, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.SPADES)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.pointsContract(160, Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.SPADES)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.capotContract(Suit.DIAMONDS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.SPADES)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.HEARTS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.CLUBS)),
                            MoveBidding.contractMove(Contract.generaleContract(Suit.DIAMONDS))))),
                new TestCase(
                    "Opponent has contract - coinched",
                    new GameStateBidding(
                        p4,
                        null,
                        Contract.pointsContract(100, Suit.CLUBS).withPlayer(p1),
                        true,
                        false),
                    new ArrayList<>(List.of(MoveBidding.passMove())))));
    testCases.forEach(TestCase::run);
  }
}
