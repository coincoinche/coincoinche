package com.coincoinche.ratingplayer;

import java.util.List;

/** Team of Elo players. */
public class EloTeam {

  private List<EloPlayer> players;

  EloTeam(List<EloPlayer> players) {
    this.players = players;
  }

  public List<EloPlayer> getPlayers() {
    return players;
  }

  public int getRating() {
    return players.get(0).getRating() + players.get(1).getRating();
  }
}
