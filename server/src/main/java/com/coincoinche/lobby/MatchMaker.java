package com.coincoinche.lobby;

import com.coincoinche.websockets.controllers.LobbyController;
import java.util.LinkedList;

public class MatchMaker {
  private LobbyController controller;
  private LinkedList<String> playerQueue;

  public MatchMaker(LobbyController controller) {
    this.controller = controller;
    this.playerQueue = new LinkedList<>();
  }

  public void register(String username) {
    this.playerQueue.add(username);
    this.startGameIfPossible();
  }

  private void startGameIfPossible() {
    if (playerQueue.size() > 3) {
      this.playerQueue.removeFirst();
      this.playerQueue.removeFirst();
      this.playerQueue.removeFirst();
      this.playerQueue.removeFirst();
      this.controller.gameStart();
    }
  }
}
