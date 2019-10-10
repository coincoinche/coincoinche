package com.coincoinche.store;

import com.coincoinche.engine.CoincheGame;

public interface GameStore {
  // TODO better format to store a game.
  public void saveGame(String gameId, CoincheGame game);

  public CoincheGame getGame(String gameId);
}
