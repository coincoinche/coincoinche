package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.cards.ValuedCard;
import com.coincoinche.engine.teams.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/** Unit tests for tricks. */
public class TrickTest extends GameEngineTestHelper {

  @Before
  public void setUp() {
    createTeams();
  }

  @Test
  public void getHighestTrump() {
    class TestCase extends GameEngineTestHelper.TestCase {
      private Player currentPlayer;
      private Suit trumpSuit;
      private Map<Integer, List<String>> cards;
      private ValuedCard expected;
      private boolean nullExpected = false;

      TestCase(
          String name,
          Player currentPlayer,
          Suit trumpSuit,
          Map<Integer, List<String>> cards,
          ValuedCard expected) {
        super(name);
        this.currentPlayer = currentPlayer;
        this.trumpSuit = trumpSuit;
        this.cards = cards;
        this.expected = expected;
      }

      TestCase nullExpected() {
        this.nullExpected = true;
        return this;
      }

      @Override
      protected void runAssertions() {
        assignCards(cards);
        Trick currentTrick = createTrick(currentPlayer, trumpSuit);
        if (nullExpected) {
          assertThat(currentTrick.getHighestTrump())
              .as("Check trick's highest trump is null")
              .isNull();
        } else {
          assertThat(currentTrick.getHighestTrump())
              .as("Check trick's highest trump is correct")
              .isEqualTo(expected);
        }
      }
    }
    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase(
                        "No trump - return null",
                        p1,
                        Suit.CLUBS,
                        Map.of(
                            2, List.of("As"),
                            3, List.of("7s"),
                            4, List.of("Ts"),
                            1, List.of()),
                        null)
                    .nullExpected(),
                new TestCase(
                    "Trump C - 7h,Qh,Tc - return Tc",
                    p1,
                    Suit.CLUBS,
                    Map.of(
                        2, List.of("7h"),
                        3, List.of("Qh"),
                        4, List.of("Tc"),
                        1, List.of()),
                    ValuedCard.fromCard(stringToCard("Tc"), true)),
                new TestCase(
                    "Trump C - 7c,9h,Tc return Tc",
                    p1,
                    Suit.CLUBS,
                    Map.of(
                        2, List.of("7c"),
                        3, List.of("9h"),
                        4, List.of("Tc"),
                        1, List.of()),
                    ValuedCard.fromCard(stringToCard("Tc"), true)),
                new TestCase(
                    "Trump C - 7c,9c,Tc return 9c",
                    p1,
                    Suit.CLUBS,
                    Map.of(
                        2, List.of("7c"),
                        3, List.of("9c"),
                        4, List.of("Tc"),
                        1, List.of()),
                    ValuedCard.fromCard(stringToCard("9c"), true))));
    testCases.forEach(TestCase::run);
  }

  @Test
  public void getMaster() {
    class TestCase extends GameEngineTestHelper.TestCase {
      private Player currentPlayer;
      private Suit trumpSuit;
      private Map<Integer, List<String>> cards;
      private Player expected;

      TestCase(
          String name,
          Player currentPlayer,
          Suit trumpSuit,
          Map<Integer, List<String>> cards,
          Player expected) {
        super(name);
        this.currentPlayer = currentPlayer;
        this.trumpSuit = trumpSuit;
        this.cards = cards;
        this.expected = expected;
      }

      @Override
      protected void runAssertions() {
        assignCards(cards);
        Trick currentTrick = createTrick(currentPlayer, trumpSuit);
        assertThat(currentTrick.getMaster())
            .as("Check trick's master is correct")
            .isEqualTo(expected);
      }
    }
    List<TestCase> testCases =
        new ArrayList<>(
            List.of(
                new TestCase(
                    "First player is master - no trump",
                    p1,
                    Suit.SPADES,
                    Map.of(
                        2, List.of(),
                        3, List.of(),
                        4, List.of("Ah"),
                        1, List.of()),
                    p4),
                new TestCase(
                    "First player is master - trump",
                    p1,
                    Suit.SPADES,
                    Map.of(
                        2, List.of(),
                        3, List.of(),
                        4, List.of("As"),
                        1, List.of()),
                    p4),
                new TestCase(
                    "P3: As, P4: 9s - P3 is master",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of(),
                        3, List.of("As"),
                        4, List.of("9s"),
                        1, List.of()),
                    p3),
                new TestCase(
                    "P3: As (trump), P4: 9s - P4 is master",
                    p1,
                    Suit.SPADES,
                    Map.of(
                        2, List.of(),
                        3, List.of("As"),
                        4, List.of("9s"),
                        1, List.of()),
                    p4),
                new TestCase(
                    "P3: Ts, P4: As - P4 is master",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of(),
                        3, List.of("Ts"),
                        4, List.of("As"),
                        1, List.of()),
                    p4),
                new TestCase(
                    "P3: Ts, P4: 7h (trump) - P4 is master",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of(),
                        3, List.of("Ts"),
                        4, List.of("7h"),
                        1, List.of()),
                    p4),
                new TestCase(
                    "P3: Ts, P4: 9h - P3 is master",
                    p1,
                    Suit.CLUBS,
                    Map.of(
                        2, List.of(),
                        3, List.of("Ts"),
                        4, List.of("9h"),
                        1, List.of()),
                    p3),
                new TestCase(
                    "P2: Ts, P3: Ks, P4: Qs - P2 is master",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of("Ts"),
                        3, List.of("Ks"),
                        4, List.of("Qs"),
                        1, List.of()),
                    p2),
                new TestCase(
                    "P2: Ts, P3: Ks, P4: As - P4 is master",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of("Ts"),
                        3, List.of("Ks"),
                        4, List.of("As"),
                        1, List.of()),
                    p4),
                new TestCase(
                    "P2: Ts, P3: Ks, P4: Ac - P2 is master",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of("Ts"),
                        3, List.of("Ks"),
                        4, List.of("Ac"),
                        1, List.of()),
                    p2),
                new TestCase(
                    "P2: Ts, P3: 7h (trump), P4: Qs - P3 is master",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of("Ts"),
                        3, List.of("7h"),
                        4, List.of("Qs"),
                        1, List.of()),
                    p3),
                new TestCase(
                    "P2: Ts, P3: 7h (trump), P4: Jh - P4 is master",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of("Ts"),
                        3, List.of("7h"),
                        4, List.of("Jh"),
                        1, List.of()),
                    p4)));
    testCases.forEach(TestCase::run);
  }
}
