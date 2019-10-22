package com.coincoinche.engine;

/** Interface representing the state of a game phase transitioning to another game phase. */
public interface GameStateTransition extends GameState {
  GameState createNextGameState();
}
