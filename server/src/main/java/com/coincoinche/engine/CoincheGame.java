package com.coincoinche.engine;

import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.game.RedBlueRotatingPlayersGame;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/** Implementation of a coinche game. */
public class CoincheGame extends RedBlueRotatingPlayersGame<Player> {

  public enum Phase {
    BIDDING("BIDDING"),
    MAIN("MAIN");

    private String name;

    Phase(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public static final int MAX_TRICKS_POINTS = 162;
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
    if (result.isDraw()) {
      startNextRound();
      return GameResult.unfinishedResult();
    }
    // update teams' points
    for (Entry<Team, Integer> entry : result.getTeamsPoints().entrySet()) {
      entry.getKey().addPoints(entry.getValue());
    }
    if (isFinished()) {
      return GameResult.finishedResult(
          Map.of((Team) redTeam, redTeam.getPoints(), (Team) blueTeam, blueTeam.getPoints()));
    }
    startNextRound();
    return GameResult.unfinishedResult();
  }

  private boolean isFinished() {
    int redPoints = redTeam.getPoints();
    int bluePoints = blueTeam.getPoints();
    if (redPoints >= WINNING_POINTS && redPoints > bluePoints) {
      return true;
    }
    if (bluePoints >= WINNING_POINTS && bluePoints > redPoints) {
      return true;
    }
    return false;
  }

  private void startNextRound() {
    rotatePlayers();
    initializeGameRound();
  }

  private void initializeGameRound() {
    int index = getCurrentPlayerIndex();
    currentRound = new CoincheGameRound((Team) redTeam, (Team) blueTeam);
    currentRound.setCurrentPlayerIndex(index);
    currentRound.setGlobalGame(this);
    // initialize first state of the game (bidding phase)
    GameStateBidding state =
        GameStateBidding.initialGameStateBidding(currentRound.getCurrentPlayer());
    List<Team> teams = new ArrayList<Team>(List.of((Team) redTeam, (Team) blueTeam));
    state.setTeams(teams);
    currentRound.setState(state);
  }

  public CoincheGameRound getCurrentRound() {
    return currentRound;
  }

  void setCurrentRound(CoincheGameRound currentRound) {
    this.currentRound = currentRound;
  }

  /**
   * Get a player by his username.
   *
   * @param username - username of the player to retrieve.
   * @return the retrieved player.
   */
  public Player getPlayer(String username) {
    for (Player player : this.getBlueTeam().getPlayers()) {
      if (player.getUsername().equals(username)) {
        return player;
      }
    }

    for (Player player : this.getRedTeam().getPlayers()) {
      if (player.getUsername().equals(username)) {
        return player;
      }
    }

    throw new IllegalArgumentException("Player not found with this username");
  }

  /**
   * Get the current phase of the game, i.e. BIDDING or MAIN.
   *
   * @return the current phase.
   */
  public Phase getCurrentRoundPhase() {
    if (this.getCurrentRound().getState() instanceof GameStateBidding) {
      return Phase.BIDDING;
    }
    return Phase.MAIN;
  }
}
