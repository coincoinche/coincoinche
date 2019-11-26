package com.coincoinche.engine;

import com.coincoinche.engine.teams.Team;
import java.util.Map;

/** Interface representing the state of a terminal game phase. */
public interface GameStateTerminal extends GameState {
  public Map<Team, Integer> getTeamsPoints();
}
