package com.coincoinche.engine.game;

import java.util.List;

/** Interface for a team of players. */
public interface TeamInterface<P> {

  List<P> getPlayers();

  int getPoints();
}
