package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.teams.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/** Unit tests for the playing game. */
public class GameStatePlayingTest extends GameEngineTestHelper {

  private class TestCase extends GameEngineTestHelper.TestCase {
    private Player currentPlayer;
    private Suit trumpSuit;
    private Map<Integer, List<String>> cards;
    private List<MovePlaying> expected;

    TestCase(
        String name,
        Player currentPlayer,
        Suit trumpSuit,
        Map<Integer, List<String>> cards,
        List<MovePlaying> expected) {
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
      System.out.println(currentTrick.toString());
      GameStatePlaying state = new GameStatePlaying(currentPlayer, trumpSuit, currentTrick, 1);
      List<Move> actual = state.getLegalMoves();
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
                    "First player: can play any card",
                    p1,
                    Suit.HEARTS,
                    Map.of(
                        2, List.of(),
                        3, List.of(),
                        4, List.of(),
                        1, List.of("7h", "Qh", "Qc", "Tc", "Kc", "8d", "Jh", "9h")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("7h")),
                            MovePlaying.cardMove(stringToCard("9h")),
                            MovePlaying.cardMove(stringToCard("Jh")),
                            MovePlaying.cardMove(stringToCard("Qh")),
                            MovePlaying.cardMove(stringToCard("Tc")),
                            MovePlaying.cardMove(stringToCard("Qc")),
                            MovePlaying.cardMove(stringToCard("Kc")),
                            MovePlaying.cardMove(stringToCard("8d"))))),
                new TestCase(
                    "P1: clubs - p2 can only play clubs",
                    p2,
                    Suit.HEARTS,
                    Map.of(
                        3, List.of(),
                        4, List.of(),
                        1, List.of("Ac"),
                        2, List.of("Tc", "Kc", "7h", "9h", "Th", "Ks", "As")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("Tc")),
                            MovePlaying.cardMove(stringToCard("Kc"))))),
                new TestCase(
                    "P1: clubs - p2 w/ no club must use trump",
                    p2,
                    Suit.HEARTS,
                    Map.of(
                        3, List.of(),
                        4, List.of(),
                        1, List.of("Ac"),
                        2, List.of("7h", "9h", "Th", "Ks", "As")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("7h")),
                            MovePlaying.cardMove(stringToCard("9h")),
                            MovePlaying.cardMove(stringToCard("Th"))))),
                new TestCase(
                    "P1: clubs - p2 w/ no club no trump can play any card",
                    p2,
                    Suit.HEARTS,
                    Map.of(
                        3, List.of(),
                        4, List.of(),
                        1, List.of("Ac"),
                        2, List.of("Ks", "As", "9d")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("Ks")),
                            MovePlaying.cardMove(stringToCard("As")),
                            MovePlaying.cardMove(stringToCard("9d"))))),
                new TestCase(
                    "P1: trump - p2 can only play higher trumps",
                    p2,
                    Suit.SPADES,
                    Map.of(
                        3, List.of(),
                        4, List.of(),
                        1, List.of("Ts"),
                        2, List.of("9s", "Js", "8s", "Ks", "Ah", "Qc")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("9s")),
                            MovePlaying.cardMove(stringToCard("Js"))))),
                new TestCase(
                    "P1: trump - p2 w/ no higher trump must use trump",
                    p2,
                    Suit.SPADES,
                    Map.of(
                        3, List.of(),
                        4, List.of(),
                        1, List.of("Js"),
                        2, List.of("9s", "Ts", "8s", "Ks", "Ah", "Qc")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("8s")),
                            MovePlaying.cardMove(stringToCard("9s")),
                            MovePlaying.cardMove(stringToCard("Ts")),
                            MovePlaying.cardMove(stringToCard("Ks"))))),
                new TestCase(
                    "P1: trump - p2 w/ no trump can play any card",
                    p2,
                    Suit.SPADES,
                    Map.of(
                        3, List.of(),
                        4, List.of(),
                        1, List.of("Js"),
                        2, List.of("Ah", "Qc", "7d", "Tc", "9h")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("9h")),
                            MovePlaying.cardMove(stringToCard("Ah")),
                            MovePlaying.cardMove(stringToCard("Tc")),
                            MovePlaying.cardMove(stringToCard("Qc")),
                            MovePlaying.cardMove(stringToCard("7d"))))),
                new TestCase(
                    "P1: clubs, P2: clubs - p3 can only play clubs",
                    p3,
                    Suit.SPADES,
                    Map.of(
                        4, List.of(),
                        1, List.of("Ac"),
                        2, List.of("Kc"),
                        3, List.of("Tc", "9c", "7c", "Ts", "9s", "Ah", "Kh")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("7c")),
                            MovePlaying.cardMove(stringToCard("9c")),
                            MovePlaying.cardMove(stringToCard("Tc"))))),
                new TestCase(
                    "P1: clubs master, P2: clubs - p3 w/ no club can play any card",
                    p3,
                    Suit.SPADES,
                    Map.of(
                        4, List.of(),
                        1, List.of("Ac"),
                        2, List.of("Kc"),
                        3, List.of("Ts", "9s", "Ah", "Kh")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("9s")),
                            MovePlaying.cardMove(stringToCard("Ts")),
                            MovePlaying.cardMove(stringToCard("Kh")),
                            MovePlaying.cardMove(stringToCard("Ah"))))),
                new TestCase(
                    "P1: clubs, P2: clubs master - p3 w/ no club must use trump",
                    p3,
                    Suit.SPADES,
                    Map.of(
                        4, List.of(),
                        1, List.of("Tc"),
                        2, List.of("Ac"),
                        3, List.of("Ts", "9s", "Ah", "Kh")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("9s")),
                            MovePlaying.cardMove(stringToCard("Ts"))))),
                new TestCase(
                    "P1: clubs, P2: clubs master - p3 w/ no club no trump can play any card",
                    p3,
                    Suit.SPADES,
                    Map.of(
                        4, List.of(),
                        1, List.of("Tc"),
                        2, List.of("Ac"),
                        3, List.of("Td", "9d", "Ah", "Kd")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("Ah")),
                            MovePlaying.cardMove(stringToCard("9d")),
                            MovePlaying.cardMove(stringToCard("Td")),
                            MovePlaying.cardMove(stringToCard("Kd"))))),
                new TestCase(
                    "P1: clubs, P2: trump - p3 can only play clubs",
                    p3,
                    Suit.SPADES,
                    Map.of(
                        4, List.of(),
                        1, List.of("Ac"),
                        2, List.of("As"),
                        3, List.of("Tc", "9c", "7c", "Ts", "9s", "Ah", "Kh")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("7c")),
                            MovePlaying.cardMove(stringToCard("9c")),
                            MovePlaying.cardMove(stringToCard("Tc"))))),
                new TestCase(
                    "P1: clubs, P2: trump - p3 w/ no club must play higher trump",
                    p3,
                    Suit.SPADES,
                    Map.of(
                        4, List.of(),
                        1, List.of("Ac"),
                        2, List.of("As"),
                        3, List.of("Ts", "9s", "Ah", "Kh")),
                    new ArrayList<>(List.of(MovePlaying.cardMove(stringToCard("9s"))))),
                new TestCase(
                    "P1: clubs, P2: trump - p3 w/ no club no trump can play any card",
                    p3,
                    Suit.SPADES,
                    Map.of(
                        4, List.of(),
                        1, List.of("Ac"),
                        2, List.of("As"),
                        3, List.of("9d", "Ah", "Kh")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("Kh")),
                            MovePlaying.cardMove(stringToCard("Ah")),
                            MovePlaying.cardMove(stringToCard("9d"))))),
                new TestCase(
                    "P1: clubs, P2: trump master, P3:trump - p4 must play clubs",
                    p4,
                    Suit.SPADES,
                    Map.of(
                        1, List.of("Ac"),
                        2, List.of("9s"),
                        3, List.of("Ts"),
                        4, List.of("Tc", "Js", "7s", "8s", "Ah", "Kd", "8d")),
                    new ArrayList<>(List.of(MovePlaying.cardMove(stringToCard("Tc"))))),
                new TestCase(
                    "P1: clubs, P2: trump master, P3:trump - p4 w/ no club can play any card but no lower trump",
                    p4,
                    Suit.SPADES,
                    Map.of(
                        1, List.of("Ac"),
                        2, List.of("As"),
                        3, List.of("7s"),
                        4, List.of("9s", "Js", "Ts", "8s", "Ah", "Kd", "8d")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("9s")),
                            MovePlaying.cardMove(stringToCard("Js")),
                            MovePlaying.cardMove(stringToCard("Ah")),
                            MovePlaying.cardMove(stringToCard("8d")),
                            MovePlaying.cardMove(stringToCard("Kd"))))),
                new TestCase(
                    "P1: clubs, P2: trump master, P3:trump - p4 w/ no club no higher trump can play any card",
                    p4,
                    Suit.SPADES,
                    Map.of(
                        1, List.of("Ac"),
                        2, List.of("As"),
                        3, List.of("7s"),
                        4, List.of("Ts", "8s", "Ah", "Kd", "8d")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("8s")),
                            MovePlaying.cardMove(stringToCard("Ts")),
                            MovePlaying.cardMove(stringToCard("Ah")),
                            MovePlaying.cardMove(stringToCard("8d")),
                            MovePlaying.cardMove(stringToCard("Kd"))))),
                new TestCase(
                    "P1: clubs, P2: clubs, P3: clubs master - p4 w/ no club must play trump",
                    p4,
                    Suit.SPADES,
                    Map.of(
                        1, List.of("Tc"),
                        2, List.of("9c"),
                        3, List.of("Ac"),
                        4, List.of("Js", "7s", "8s", "Ah", "Kd", "8d")),
                    new ArrayList<>(
                        List.of(
                            MovePlaying.cardMove(stringToCard("7s")),
                            MovePlaying.cardMove(stringToCard("8s")),
                            MovePlaying.cardMove(stringToCard("Js")))))));

    testCases.forEach(TestCase::run);
  }
}
