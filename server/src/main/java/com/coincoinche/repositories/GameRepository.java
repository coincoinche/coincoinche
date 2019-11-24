package com.coincoinche.repositories;

import com.coincoinche.engine.CoincheGame;

public interface GameRepository {
  // TODO better format to repositories a game.
  public void saveGame(String gameId, CoincheGame game);

  public CoincheGame getGame(String gameId);

  public void removeGame(String gameId);
}
