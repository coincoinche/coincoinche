package com.coincoinche.repositories;

import com.coincoinche.engine.CoincheGame;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryGameRepository implements GameRepository {
  private Map<String, CoincheGame> repository;

  public InMemoryGameRepository() {
    this.repository = new ConcurrentHashMap<>();
  }

  public void saveGame(String gameId, CoincheGame game) {
    this.repository.put(gameId, game);
  }

  public CoincheGame getGame(String gameId) {
    return this.repository.get(gameId);
  }

  public void removeGame(String gameId) {
    this.repository.remove(gameId);
  }
}
