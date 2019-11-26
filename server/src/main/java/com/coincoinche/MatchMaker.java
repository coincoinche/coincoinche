package com.coincoinche;

import com.coincoinche.websockets.controllers.LobbyController;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MatchMaker {
  private LobbyController controller;
  private Queue<String> playerQueue;

  public MatchMaker(LobbyController controller) {
    this.controller = controller;
    this.playerQueue = new ConcurrentLinkedQueue<>();
  }

  public void register(String username) {
    // same username can't be in the lobby several times
    if (this.playerQueue.contains(username)) {
      return;
    }
    this.playerQueue.add(username);
    this.startGameIfPossible();
  }

  public void remove(String username) {
    this.playerQueue.removeIf((String usernameInQueue) -> usernameInQueue.equals(username));
  }

  private void startGameIfPossible() {
    if (playerQueue.size() > 3) {
      String[] usernames = {
        this.playerQueue.remove(),
        this.playerQueue.remove(),
        this.playerQueue.remove(),
        this.playerQueue.remove()
      };
      this.controller.gameStart(usernames);
    }
  }
}
