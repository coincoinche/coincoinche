package com.coincoinche.store;

import com.coincoinche.engine.CoincheGame;
import java.util.HashMap;

public class InMemoryGameStore implements GameStore {
  private HashMap<String, CoincheGame> store;

  public InMemoryGameStore() {
    this.store = new HashMap<>();
  }

  public void saveGame(String gameId, CoincheGame game) {
    this.store.put(gameId, game);
  }

  public CoincheGame getGame(String gameId) {
    return this.store.get(gameId);
  }
}
