package com.coincoinche.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.cards.Rank;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;

/**
 * Several helper methods and classes to help with testing the game engine.
 */
public class GameEngineTestHelper {

  protected Player p1;
  protected Player p2;
  protected Player p3;
  protected Player p4;

  protected Team t1;
  protected Team t2;


  public static abstract class TestCase {
    private String name;

    TestCase(String name) {
      this.name = name;
    }

    public void run() {
      try {
        runAssertions();
      } catch (AssertionError e) {
        throw new Error("Error in " + name, e);
      }
    }

    protected abstract void runAssertions();
  }

  public static abstract class ComparisonTestCase<T> extends TestCase {
    protected T o1;
    protected T o2;
    protected boolean expected;

    ComparisonTestCase(String name, T o1, T o2, boolean expected) {
      super(name);
      this.o1 = o1;
      this.o2 = o2;
      this.expected = expected;
    }

  }

  /**
   * Create two teams of two players.
   */
  protected void createTeams() {
    p1 = new Player("Player 1");
    p2 = new Player("Player 2");
    p3 = new Player("Player 3");
    p4 = new Player("Player 4");
    t1 = new Team(p1, p3, Team.Color.BLUE);
    t2 = new Team(p2, p4, Team.Color.RED);
  }

  /**
   * Assign cards to players according to a map.
   * <strong>Teams should be created before this method is called.</strong>
   * @param cards maps an integer representing the player to a list of strings
   * representing the cards. Typical strings would be:
   * <ul>
   *  <li>Ah -> ace of hearts</li>
   *  <li>5c -> five of clubs</li>
   *  <li>Td -> ten of diamonds</li>
   *  <li>Qs -> queen of spades</li>
   * </ul>
   */
  protected void assignCards(Map<Integer, List<String>> cards) {
    Map<Integer, Player> players = new HashMap<>();
    players.put(1, p1);
    players.put(2, p2);
    players.put(3, p3);
    players.put(4, p4);
    // assign cards to players
    for (int playerKey : cards.keySet()) {
      for (String cardString : cards.get(playerKey)) {
        players.get(playerKey).addCard(stringToCard(cardString));
      }
    }
  }


  /*
   * Private helper to convert a String to a Card.
   * e.g. Ah -> ace of hearts
   *      5c -> five of clubs
   *      Td -> ten of diamonds
   *      Qs -> queen of spades
   */
  private static Card stringToCard(String shortName) {
    char rankChar = shortName.charAt(0);
    char suitChar = shortName.charAt(1);
    Rank rank = Rank.valueOfByShortName(String.valueOf(rankChar));
    Suit suit = Suit.valueOfByShortName(String.valueOf(suitChar));
    return new Card(suit, rank);
  }

}