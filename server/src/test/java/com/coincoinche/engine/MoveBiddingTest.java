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
