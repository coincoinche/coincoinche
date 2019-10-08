package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.cards.Suit;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** Unit tests for the bidding moves. */
public class BiddingMoveTest {

  @Test
  public void compareTo() {

    class TestCase extends GameEngineTestHelper.ComparisonTestCase<BiddingMove> {
      int expected;

      TestCase(String name, BiddingMove m1, BiddingMove m2, int expected) {
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
                new TestCase("pass - pass", BiddingMove.passMove(), BiddingMove.passMove(), 0),
                new TestCase(
                    "coinche - coinche", BiddingMove.coincheMove(), BiddingMove.coincheMove(), 0),
                new TestCase(
                    "surcoinche - surcoinche",
                    BiddingMove.surcoincheMove(),
                    BiddingMove.surcoincheMove(),
                    0),
                new TestCase(
                    "pass - coinche", BiddingMove.passMove(), BiddingMove.coincheMove(), -1),
                new TestCase(
                    "surcoinche - coinche",
                    BiddingMove.surcoincheMove(),
                    BiddingMove.coincheMove(),
                    1),
                new TestCase(
                    "surcoinche - pass", BiddingMove.surcoincheMove(), BiddingMove.passMove(), 1),
                new TestCase(
                    "pass - capot heart",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    -1),
                new TestCase(
                    "pass - 130 spades",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    -1),
                new TestCase(
                    "coinche - capot heart",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    -1),
                new TestCase(
                    "130 spades - pass",
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    BiddingMove.passMove(),
                    1),
                new TestCase(
                    "130 spades - capot heart",
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    -1),
                new TestCase(
                    "130 heart - 130 spades",
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    1),
                new TestCase(
                    "capot heart - capot heart",
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    0),
                new TestCase(
                    "generale clubs - generale clubs",
                    BiddingMove.contractMove(Contract.generaleContract(Suit.CLUBS)),
                    BiddingMove.contractMove(Contract.generaleContract(Suit.CLUBS)),
                    0),
                new TestCase(
                    "capot clubs - capot spades",
                    BiddingMove.contractMove(Contract.capotContract(Suit.CLUBS)),
                    BiddingMove.contractMove(Contract.capotContract(Suit.SPADES)),
                    1),
                new TestCase(
                    "capot heart - generale spades",
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.generaleContract(Suit.SPADES)),
                    -1),
                new TestCase(
                    "generale heart - generale diamonds",
                    BiddingMove.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.generaleContract(Suit.DIAMONDS)),
                    -1),
                new TestCase(
                    "90 heart - 90 heart",
                    BiddingMove.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    0),
                new TestCase(
                    "90 heart - 130 heart",
                    BiddingMove.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                    -1),
                new TestCase(
                    "110 diamonds - 110 spades",
                    BiddingMove.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
                    BiddingMove.contractMove(Contract.pointsContract(110, Suit.SPADES)),
                    1),
                new TestCase(
                    "capot heart - 100 heart",
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(100, Suit.HEARTS)),
                    1),
                new TestCase(
                    "generale heart - 140 diamonds",
                    BiddingMove.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(140, Suit.DIAMONDS)),
                    1)));
    testCases.forEach(TestCase::run);
  }

  @Test
  public void equals() {

    class TestCase extends GameEngineTestHelper.ComparisonTestCase<BiddingMove> {

      TestCase(String name, BiddingMove m1, BiddingMove m2, boolean expected) {
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
                new TestCase("pass - pass", BiddingMove.passMove(), BiddingMove.passMove(), true),
                new TestCase(
                    "coinche - coinche",
                    BiddingMove.coincheMove(),
                    BiddingMove.coincheMove(),
                    true),
                new TestCase(
                    "surcoinche - surcoinche",
                    BiddingMove.surcoincheMove(),
                    BiddingMove.surcoincheMove(),
                    true),
                new TestCase(
                    "pass - coinche", BiddingMove.passMove(), BiddingMove.coincheMove(), false),
                new TestCase(
                    "surcoinche - coinche",
                    BiddingMove.surcoincheMove(),
                    BiddingMove.coincheMove(),
                    false),
                new TestCase(
                    "surcoinche - pass",
                    BiddingMove.surcoincheMove(),
                    BiddingMove.passMove(),
                    false),
                new TestCase(
                    "pass - capot heart",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    false),
                new TestCase(
                    "pass - 130 spades",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    false),
                new TestCase(
                    "coinche - capot heart",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    false),
                new TestCase(
                    "coinche - 130 spades",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    false),
                new TestCase(
                    "surcoinche - capot heart",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    false),
                new TestCase(
                    "surcoinche - 130 spades",
                    BiddingMove.passMove(),
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
                    false),
                new TestCase(
                    "capot heart - capot heart",
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    true),
                new TestCase(
                    "generale clubs - generale clubs",
                    BiddingMove.contractMove(Contract.generaleContract(Suit.CLUBS)),
                    BiddingMove.contractMove(Contract.generaleContract(Suit.CLUBS)),
                    true),
                new TestCase(
                    "capot heart - capot spades",
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.capotContract(Suit.SPADES)),
                    false),
                new TestCase(
                    "capot heart - generale heart",
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    false),
                new TestCase(
                    "generale heart - generale diamonds",
                    BiddingMove.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.generaleContract(Suit.DIAMONDS)),
                    false),
                new TestCase(
                    "90 heart - 90 heart",
                    BiddingMove.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    true),
                new TestCase(
                    "90 heart - 130 heart",
                    BiddingMove.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
                    false),
                new TestCase(
                    "110 diamonds - 110 spades",
                    BiddingMove.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
                    BiddingMove.contractMove(Contract.pointsContract(110, Suit.SPADES)),
                    false),
                new TestCase(
                    "capot heart - 100 heart",
                    BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(100, Suit.HEARTS)),
                    false),
                new TestCase(
                    "generale heart - 140 heart",
                    BiddingMove.contractMove(Contract.generaleContract(Suit.HEARTS)),
                    BiddingMove.contractMove(Contract.pointsContract(140, Suit.HEARTS)),
                    false)));
    testCases.forEach(TestCase::run);
  }
}
