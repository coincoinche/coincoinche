package com.coincoinche.events;

import com.coincoinche.engine.cards.Card;
import java.util.List;

public class RoundStartedEvent extends Event {
  private List<Card> playerCards;

  public RoundStartedEvent(List<Card> playerCards) {
    super(EventType.ROUND_STARTED);
    this.playerCards = playerCards;
  }

  public List<Card> getPlayerCards() {
    return playerCards;
  }
}
