package com.coincoinche.engine.game;

import java.util.List;

/** Interface for a team of players. */
public interface TeamInterface<P> {

  public List<P> getPlayers();

  public int getPoints();
}
