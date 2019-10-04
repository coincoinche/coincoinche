package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.coincoinche.engine.cards.Suit;

import org.junit.Test;


/**
 * Unit tests for the bidding moves.
 */
public class BiddingMoveTest {

  @Test
  public void equals() {

    class TestCase extends GameEngineTestHelper.TestCase {
      private BiddingMove moveOne;
      private BiddingMove moveTwo;
      private boolean expected;

      TestCase(String name, BiddingMove moveOne, BiddingMove moveTwo, boolean expected) {
        super(name);
        this.moveOne = moveOne;
        this.moveTwo = moveTwo;
        this.expected = expected;
      }

      @Override
      protected void runAssertions() {
        assertThat(moveOne.equals(moveTwo)).as("Check move equality").isEqualTo(expected);
      }

    }

    List<TestCase> testCases = new ArrayList<>(List.of(
      new TestCase(
        "pass - pass",
        BiddingMove.passMove(),
        BiddingMove.passMove(),
        true
      ),
      new TestCase(
        "coinche - coinche",
        BiddingMove.coincheMove(),
        BiddingMove.coincheMove(),
        true
      ),
      new TestCase(
        "surcoinche - surcoinche",
        BiddingMove.surcoincheMove(),
        BiddingMove.surcoincheMove(),
        true
      ),
      new TestCase(
        "pass - coinche",
        BiddingMove.passMove(),
        BiddingMove.coincheMove(),
        false
      ),
      new TestCase(
        "surcoinche - coinche",
        BiddingMove.surcoincheMove(),
        BiddingMove.coincheMove(),
        false
      ),
      new TestCase(
        "surcoinche - pass",
        BiddingMove.surcoincheMove(),
        BiddingMove.passMove(),
        false
      ),
      new TestCase(
        "pass - capot heart",
        BiddingMove.passMove(),
        BiddingMove.contractMove(
          Contract.capotContract(Suit.HEARTS)
        ),
        false
      ),
      new TestCase(
        "pass - 130 spades",
        BiddingMove.passMove(),
        BiddingMove.contractMove(
          Contract.pointsContract(130, Suit.SPADES)
        ),
        false
      ),
      new TestCase(
        "coinche - capot heart",
        BiddingMove.passMove(),
        BiddingMove.contractMove(
          Contract.capotContract(Suit.HEARTS)
        ),
        false
      ),
      new TestCase(
        "coinche - 130 spades",
        BiddingMove.passMove(),
        BiddingMove.contractMove(
          Contract.pointsContract(130, Suit.SPADES)
        ),
        false
      ),
      new TestCase(
        "surcoinche - capot heart",
        BiddingMove.passMove(),
        BiddingMove.contractMove(
          Contract.capotContract(Suit.HEARTS)
        ),
        false
      ),
      new TestCase(
        "surcoinche - 130 spades",
        BiddingMove.passMove(),
        BiddingMove.contractMove(
          Contract.pointsContract(130, Suit.SPADES)
        ),
        false
      ),
      new TestCase(
        "capot heart - capot heart",
        BiddingMove.contractMove(
          Contract.capotContract(Suit.HEARTS)
        ),
        BiddingMove.contractMove(
          Contract.capotContract(Suit.HEARTS)
        ),
        true
      ),
      new TestCase(
        "generale clubs - generale clubs",
        BiddingMove.contractMove(
          Contract.generaleContract(Suit.CLUBS)
        ),
        BiddingMove.contractMove(
          Contract.generaleContract(Suit.CLUBS)
        ),
        true
      ),
      new TestCase(
        "capot heart - capot spades",
        BiddingMove.contractMove(
          Contract.capotContract(Suit.HEARTS)
        ),
        BiddingMove.contractMove(
          Contract.capotContract(Suit.SPADES)
        ),
        false
      ),
      new TestCase(
        "capot heart - generale heart",
        BiddingMove.contractMove(
          Contract.capotContract(Suit.HEARTS)
        ),
        BiddingMove.contractMove(
          Contract.generaleContract(Suit.HEARTS)
        ),
        false
      ),
      new TestCase(
        "generale heart - generale diamonds",
        BiddingMove.contractMove(
          Contract.generaleContract(Suit.HEARTS)
        ),
        BiddingMove.contractMove(
          Contract.generaleContract(Suit.DIAMONDS)
        ),
        false
      ),
      new TestCase(
        "90 heart - 90 heart",
        BiddingMove.contractMove(
          Contract.pointsContract(90, Suit.HEARTS)
        ),
        BiddingMove.contractMove(
          Contract.pointsContract(90, Suit.HEARTS)
        ),
        true
      ),
      new TestCase(
        "90 heart - 130 heart",
        BiddingMove.contractMove(
          Contract.pointsContract(90, Suit.HEARTS)
        ),
        BiddingMove.contractMove(
          Contract.pointsContract(130, Suit.HEARTS)
        ),
        false
      ),
      new TestCase(
        "110 diamonds - 110 spades",
        BiddingMove.contractMove(
          Contract.pointsContract(110, Suit.DIAMONDS)
        ),
        BiddingMove.contractMove(
          Contract.pointsContract(110, Suit.SPADES)
        ),
        false
      ),
      new TestCase(
        "capot heart - 100 heart",
        BiddingMove.contractMove(
          Contract.capotContract(Suit.HEARTS)
        ),
        BiddingMove.contractMove(
          Contract.pointsContract(100, Suit.HEARTS)
        ),
        false
      ),
      new TestCase(
        "generale heart - 140 heart",
        BiddingMove.contractMove(
          Contract.generaleContract(Suit.HEARTS)
        ),
        BiddingMove.contractMove(
          Contract.pointsContract(140, Suit.HEARTS)
        ),
        false
      )
    ));
    testCases.forEach(TestCase::run);
  }

}