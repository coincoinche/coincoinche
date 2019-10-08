package com.coincoinche.engine;
import com.coincoinche.engine.cards.Suit;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/** Unit tests for the bidding game. */
public class BiddingGameStateTest extends GameEngineTestHelper {

  private class TestCase extends GameEngineTestHelper.TestCase {
    private GameStateBidding input;
    private List<BiddingMove> expected;

    TestCase(String name, GameStateBidding input, List<BiddingMove> expected) {
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
    List<TestCase> testCases = new ArrayList<>(List.of(
      // TODO nockty: reshuffle when 4 players pass
      new TestCase(
        "No Contract",
        new GameStateBidding(
          p1,
          null,
          false,
          false
        ),
        new ArrayList<>(List.of(
          BiddingMove.passMove(),
          BiddingMove.contractMove(Contract.pointsContract(80, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(80, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(80, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(80, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(90, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(90, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(90, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(100, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(100, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(100, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(100, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.SPADES)),
          BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.CLUBS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.SPADES)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.HEARTS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.CLUBS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.DIAMONDS))
        ))
      ),
      new TestCase(
        "Team mate has contract - not coinched",
        new GameStateBidding(
          p1,
          Contract.pointsContract(100, Suit.CLUBS).withPlayer(p3),
          false,
          false
        ),
        new ArrayList<>(List.of(
          BiddingMove.passMove(),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.SPADES)),
          BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.CLUBS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.SPADES)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.HEARTS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.CLUBS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.DIAMONDS))
        ))
      ),
      new TestCase(
        "Team mate has contract - coinched",
        new GameStateBidding(
          p1,
          Contract.pointsContract(90, Suit.DIAMONDS).withPlayer(p3),
          true,
          false
        ),
        new ArrayList<>(List.of(
          BiddingMove.passMove(),
          BiddingMove.surcoincheMove()
        ))
      ),
      new TestCase(
        "Player has contract - coinched",
        new GameStateBidding(
          p1,
          Contract.capotContract(Suit.HEARTS).withPlayer(p1),
          true,
          false
        ),
        new ArrayList<>(List.of(
          BiddingMove.passMove(),
          BiddingMove.surcoincheMove()
        ))
      ),
      new TestCase(
        "Opponent has contract - not coinched",
        new GameStateBidding(
          p2,
          Contract.pointsContract(80, Suit.HEARTS).withPlayer(p1),
          false,
          false
        ),
        new ArrayList<>(List.of(
          BiddingMove.passMove(),
          BiddingMove.coincheMove(),
          BiddingMove.contractMove(Contract.pointsContract(90, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(90, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(90, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(90, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(100, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(100, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(100, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(100, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(110, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(120, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(130, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(140, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(150, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.SPADES)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.HEARTS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.CLUBS)),
          BiddingMove.contractMove(Contract.pointsContract(160, Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.SPADES)),
          BiddingMove.contractMove(Contract.capotContract(Suit.HEARTS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.CLUBS)),
          BiddingMove.contractMove(Contract.capotContract(Suit.DIAMONDS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.SPADES)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.HEARTS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.CLUBS)),
          BiddingMove.contractMove(Contract.generaleContract(Suit.DIAMONDS))
        ))
      ),
      new TestCase(
        "Opponent has contract - coinched",
        new GameStateBidding(
          p4,
          Contract.pointsContract(100, Suit.CLUBS).withPlayer(p1),
          true,
          false
        ),
        new ArrayList<>(List.of(
          BiddingMove.passMove()
        ))
      )
    ));
    testCases.stream().forEach(TestCase::run);
  }
}
