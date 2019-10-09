package com.coincoinche.engine;

import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.game.RedBlueRotatingPlayersGame;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;

/** Implementation of a coinche game. */
public class CoincheGame extends RedBlueRotatingPlayersGame<Player> {

  private static final int WINNING_POINTS = 1000;
  // TODO nockty: consider DI for this
  private CoincheGameRound currentRound;

  public CoincheGame(Team redTeam, Team blueTeam) {
    super(redTeam, blueTeam);
    initializeGameRound();
  }

  GameResult<Team> moveWasApplied() {
    GameResult<Team> result = currentRound.moveWasApplied();
    if (!result.isFinished()) {
      return GameResult.unfinishedResult();
    }
    result.getWinnerTeam().addPoints(result.getWinnerPoints());
    Team winnerTeam = getWinnerTeam();
    if (winnerTeam != null) {
      return GameResult.finishedResult(winnerTeam, winnerTeam.getPoints());
    }
    startNextRound();
    return GameResult.unfinishedResult();
  }

  private Team getWinnerTeam() {
    if (redTeam.getPoints() > WINNING_POINTS) {
      return (Team) redTeam;
    }
    if (blueTeam.getPoints() > WINNING_POINTS) {
      return (Team) blueTeam;
    }
    return null;
  }

  private void startNextRound() {
    rotatePlayers();
    initializeGameRound();
  }

  private void initializeGameRound() {
    int index = getCurrentPlayerIndex();
    currentRound = new CoincheGameRound((Team) redTeam, (Team) blueTeam);
    currentRound.setCurrentPlayerIndex(index);
    // initialize first state of the game (bidding phase)
    GameStateBidding state =
        GameStateBidding.initialGameStateBidding(currentRound.getCurrentPlayer());
    currentRound.setState(state);
  }

  public CoincheGameRound getCurrentRound() {
    return currentRound;
  }

  void setCurrentRound(CoincheGameRound currentRound) {
    this.currentRound = currentRound;
  }
}
