package com.coincoinche.repositories;

import com.coincoinche.engine.CoincheGame;
import java.util.HashMap;

public class InMemoryGameRepository implements GameRepository {
  private HashMap<String, CoincheGame> repository;

  public InMemoryGameRepository() {
    this.repository = new HashMap<>();
  }

  public void saveGame(String gameId, CoincheGame game) {
    this.repository.put(gameId, game);
  }

  public CoincheGame getGame(String gameId) {
    return this.repository.get(gameId);
  }
}
