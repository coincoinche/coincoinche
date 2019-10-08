package com.coincoinche.engine;

import com.coincoinche.engine.teams.Team;

/** Interface representing the state of a terminal game phase. */
public interface GameStateTerminal extends GameState {
  Team getWinnerTeam();

  int getWinnerPoints();
}
