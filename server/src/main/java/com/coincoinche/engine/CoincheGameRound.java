package com.coincoinche.engine;

import com.coincoinche.engine.cards.Deck;
import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.game.RedBlueRotatingPlayersGame;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import java.util.List;

/** Implementation of a coinche round. */
public class CoincheGameRound extends RedBlueRotatingPlayersGame<Player> {

  private GameState state;

  public CoincheGameRound(Team redTeam, Team blueTeam) {
    super(redTeam, blueTeam);
    new Deck().dealCardsToTeams(redTeam, blueTeam);
  }

  /**
   * This should be called after a move was applied to a game round. It rotates players in the round
   * and change its state if necessary.
   */
  GameResult<Team> moveWasApplied() {
    // TODO nockty: handle the case when all players pass
    // rotate players before checking if the state must change
    rotatePlayers();
    Player newPlayer = getCurrentPlayer();
    state.setCurrentPlayer(newPlayer);
    if (state.mustChange()) {
      if (state instanceof GameStateTerminal) {
        GameStateTerminal terminalState = (GameStateTerminal) state;
        Team winnerTeam = terminalState.getWinnerTeam();
        int winnerPoints = terminalState.getWinnerPoints();
        return GameResult.finishedResult(winnerTeam, winnerPoints);
        // TODO nockty: must change the round's state here if non-terminal
      }
    }
    return GameResult.unfinishedResult();
  }

  @Override
  protected Player getCurrentPlayer() {
    return super.getCurrentPlayer();
  }

  public void setState(GameState state) {
    this.state = state;
  }

  public List<Move> getLegalMoves() {
    return state.getLegalMoves();
  }

  public GameState getState() {
    return state;
  }
}
