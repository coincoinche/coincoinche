package com.coincoinche.engine;

import com.coincoinche.engine.cards.Card;
import com.coincoinche.engine.cards.Rank;
import com.coincoinche.engine.cards.Suit;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Several helper methods and classes to help with testing the game engine. */
public class GameEngineTestHelper {

  protected Player p1;
  protected Player p2;
  protected Player p3;
  protected Player p4;

  protected Team t1;
  protected Team t2;

  protected CoincheGame coincheGame;

  public abstract static class TestCase {
    private String name;

    protected TestCase(String name) {
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

  public abstract static class ComparisonTestCase<T> extends TestCase {
    protected T o1;
    protected T o2;
    protected boolean expected;

    protected ComparisonTestCase(String name, T o1, T o2, boolean expected) {
      super(name);
      this.o1 = o1;
      this.o2 = o2;
      this.expected = expected;
    }
  }

  /** Create two teams of two players. */
  protected void createTeams() {
    p1 = new Player("Player 1");
    p2 = new Player("Player 2");
    p3 = new Player("Player 3");
    p4 = new Player("Player 4");
    t1 = new Team(p1, p3, Team.Color.RED);
    t2 = new Team(p2, p4, Team.Color.BLUE);
  }

  /**
   * Create a new coinche game by using the two teans. <strong>Teams should be created before this
   * method is called.</strong>
   */
  protected void createCoincheGame() {
    coincheGame = new CoincheGame(t1, t2);
  }

  /**
   * Create a trick where the current player must make a move.
   *
   * <p><strong>Cards should be assigned to players before calling this method.</strong> Zero or one
   * card should be assigned to each player, except for the current player, who can have up to 8
   * cards. Cards assigned to each player, except for the current player, represent cards that these
   * players played during the trick. Cards assigned to the current player represent cards in the
   * hand of the current player.
   *
   * <p>A runtime exception is thrown if assigned cards are invalid.
   *
   * @param currentPlayer is the player who musts play a card in the trick.
   * @param trumpSuit is the trump suit for this trick.
   * @return the newly created trick
   */
  protected Trick createTrick(Player currentPlayer, Suit trumpSuit) {
    Trick trick = Trick.emptyTrick(trumpSuit);
    // navigate among players to create the trick
    int currentPlayerIndex = getPlayertoIntMap().get(currentPlayer);
    Map<Integer, Player> players = getIntToPlayerMap();
    for (int i = 0; i < 3; i++) {
      int index = (((currentPlayerIndex - 3 + i) % 4) + 4) % 4;
      Player player = players.get(index > 0 ? index : 4);
      if (player.getCards().isEmpty()) {
        if (!trick.isEmpty()) {
          throw new RuntimeException("Invalid cards");
        }
        continue;
      }
      trick.add(player, player.getCards().get(0));
    }
    return trick;
  }

  /**
   * Assign cards to players according to a map. <strong>Teams should be created before this method
   * is called.</strong>
   *
   * @param cards maps an integer representing the player to a list of strings representing the
   *     cards. Typical strings would be:
   *     <ul>
   *       <li>Ah -> ace of hearts
   *       <li>5c -> five of clubs
   *       <li>Td -> ten of diamonds
   *       <li>Qs -> queen of spades
   *     </ul>
   */
  protected void assignCards(Map<Integer, List<String>> cards) {
    Map<Integer, Player> players = getIntToPlayerMap();
    // assign cards to players
    for (int playerKey : cards.keySet()) {
      players.get(playerKey).clearCards();
      for (String cardString : cards.get(playerKey)) {
        players.get(playerKey).addCard(stringToCard(cardString));
      }
    }
  }

  protected Map<Integer, Player> getIntToPlayerMap() {
    Map<Integer, Player> players = new HashMap<>();
    players.put(1, p1);
    players.put(2, p2);
    players.put(3, p3);
    players.put(4, p4);
    return players;
  }

  protected Map<Player, Integer> getPlayertoIntMap() {
    Map<Player, Integer> players = new HashMap<>();
    players.put(p1, 1);
    players.put(p2, 2);
    players.put(p3, 3);
    players.put(p4, 4);
    return players;
  }

  protected Player intToPlayer(int playerIndex) {
    return getIntToPlayerMap().get(playerIndex);
  }

  /*
   * Private helper to convert a String to a Card.
   * e.g. Ah -> ace of hearts
   *      5c -> five of clubs
   *      Td -> ten of diamonds
   *      Qs -> queen of spades
   */
  protected static Card stringToCard(String shortName) {
    char rankChar = shortName.charAt(0);
    char suitChar = shortName.charAt(1);
    Rank rank = Rank.valueOfByShortName(String.valueOf(rankChar));
    Suit suit = Suit.valueOfByShortName(String.valueOf(suitChar));
    return new Card(suit, rank);
  }
}
