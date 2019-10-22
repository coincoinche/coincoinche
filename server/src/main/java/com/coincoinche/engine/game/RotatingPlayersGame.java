package com.coincoinche.engine.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Implement a game with rotating players. */
public class RotatingPlayersGame<P> {

  private List<P> players;
  private int currentPlayerIndex = 0;

  public void setPlayers(List<P> players) {
    this.players = players;
  }

  /**
   * Get the player who should play currently.
   *
   * @return currrent player.
   */
  public P getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  /** Rotate players turn. */
  public void rotatePlayers() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  public void setCurrentPlayerIndex(int currentPlayerIndex) {
    this.currentPlayerIndex = currentPlayerIndex;
  }

  /**
   * Set the current player of the game by using a player instance.
   *
   * @param player is the current player to set on the game.
   */
  public void setCurrentPlayer(P player) {
    Map<P, Integer> playersIndexes = getPlayersIndexes();
    setCurrentPlayerIndex(playersIndexes.get(player));
  }

  private Map<P, Integer> getPlayersIndexes() {
    Map<P, Integer> playersIndexes = new HashMap<>();
    for (P p : players) {
      playersIndexes.put(p, players.indexOf(p));
    }
    return playersIndexes;
  }
}
