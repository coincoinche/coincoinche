package com.coincoinche.engine;

import com.coincoinche.engine.cards.Deck;
import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.game.RedBlueRotatingPlayersGame;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import java.util.List;
import java.util.Map;

/** Implementation of a coinche round. */
public class CoincheGameRound extends RedBlueRotatingPlayersGame<Player> {
  private CoincheGame globalGame;
  private GameState state;

  protected CoincheGameRound(Team redTeam, Team blueTeam) {
    super(redTeam, blueTeam);
    new Deck().dealCardsToTeams(redTeam, blueTeam);
  }

  /**
   * This should be called after a move was applied to a game round. It rotates players in the round
   * and change its state if necessary.
   */
  protected GameResult<Team> moveWasApplied() {
    // rotate players before checking if the state must change
    state.rotatePlayers(this);
    // handle the case when all players pass the bidding phase: new round must start
    if (state instanceof GameStateBidding) {
      GameStateBidding biddingState = (GameStateBidding) state;
      // current player of the global game is first player of the round
      if (biddingState.getHighestBidding() == null
          && biddingState.getCurrentPlayer().equals(globalGame.getCurrentPlayer())) {
        return GameResult.drawResult();
      }
    }
    if (state.mustChange()) {
      if (state instanceof GameStateTerminal) {
        GameStateTerminal terminalState = (GameStateTerminal) state;
        Map<Team, Integer> teamsPoints = terminalState.getTeamsPoints();
        return GameResult.finishedResult(teamsPoints);
      }
      // update round's state type
      if (state instanceof GameStateTransition) {
        GameStateTransition transitionState = (GameStateTransition) state;
        GameState newState = transitionState.createNextGameState(this);
        this.state = newState;
        // ensure consistency of current player
        setCurrentPlayer(newState.getCurrentPlayer());
        return GameResult.unfinishedResult();
      }
    }
    return GameResult.unfinishedResult();
  }

  @Override
  public Player getCurrentPlayer() {
    return super.getCurrentPlayer();
  }

  protected void setGlobalGame(CoincheGame globalGame) {
    this.globalGame = globalGame;
  }

  protected void setState(GameState state) {
    this.state = state;
  }

  public List<Move> getLegalMoves() {
    return state.getLegalMoves();
  }

  public GameState getState() {
    return state;
  }

  protected CoincheGame getGlobalGame() {
    return globalGame;
  }
}
