package com.coincoinche.engine;

import static org.assertj.core.api.Assertions.assertThat;

import com.coincoinche.engine.game.GameResult;
import com.coincoinche.engine.teams.Team;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;

/** Unit tests for the coinche global game. */
public class CoincheGameTest extends GameEngineTestHelper {

  @Before
  public void setUp() {
    createTeams();
    createCoincheGame();
  }

  @Test
  public void initialization() {
    assertThat(coincheGame.getCurrentRound())
        .as("Check first round isn't null")
        .isNotNull();
    assertThat(coincheGame.getCurrentPlayerIndex())
        .as("Check first player index is 0")
        .isEqualTo(0);
    assertThat(coincheGame.getCurrentRound().getState())
        .as("Check first state isn't null")
        .isNotNull();
    assertThat(coincheGame.getCurrentRound().getState().getCurrentPlayer())
        .as("Check first player is p1")
        .isEqualTo(p1);
  }

  @Test
  public void moveWasApplied() {
    // mock a unfinished result for when a move was applied to the current round
    CoincheGameRound currentRound = coincheGame.getCurrentRound();
    CoincheGameRound spyRound = spy(currentRound);
    coincheGame.setCurrentRound(spyRound);
    GameResult<Team> roundResult = GameResult.unfinishedResult();
    doReturn(roundResult).when(spyRound).moveWasApplied();
    GameResult<Team> gameResult = coincheGame.moveWasApplied();
    assertThat(gameResult.isFinished())
        .as("UNFINISHED ROUND - Check game hasn't finished yet")
        .isFalse();
    // check that the round has stayed the same
    assertThat(coincheGame.getCurrentPlayerIndex())
        .as("UNFINISHED ROUND - Check player index is still 0")
        .isEqualTo(0);
    assertThat(coincheGame.getCurrentRound())
        .as("UNFINISHED ROUND - Check round is still the same")
        .isEqualTo(spyRound);
    // mock a finished result but not a finished game
    roundResult = GameResult.finishedResult(t1, 0);
    doReturn(roundResult).when(spyRound).moveWasApplied();
    gameResult = coincheGame.moveWasApplied();
    assertThat(gameResult.isFinished())
        .as("FINISHED ROUND - Check that the game hasn't finished yet")
        .isFalse();
    // check that a new round was created with the next player
    assertThat(coincheGame.getCurrentPlayerIndex())
        .as("FINISHED ROUND - Check new player index is 1")
        .isEqualTo(1);
    assertThat(coincheGame.getCurrentRound())
        .as("FINISHED ROUND - Check new round isn't null")
        .isNotNull();
    assertThat(coincheGame.getCurrentRound())
        .as("FINISHED ROUND - Check round has changed")
        .isNotEqualTo(spyRound);
    assertThat(coincheGame.getCurrentRound().getState())
        .as("FINISHED ROUND - Check new state isn't null")
        .isNotNull();
    assertThat(coincheGame.getCurrentRound().getState().getCurrentPlayer())
        .as("FINISHED ROUND - Check first player of new state is p2")
        .isEqualTo(p2);
    // mock a finished game now
    currentRound = coincheGame.getCurrentRound();
    spyRound = spy(currentRound);
    coincheGame.setCurrentRound(spyRound);
    roundResult = GameResult.finishedResult(t1, 42000);
    doReturn(roundResult).when(spyRound).moveWasApplied();
    gameResult = coincheGame.moveWasApplied();
    assertThat(gameResult.isFinished())
        .as("FINISHED GAME - Check that the game has finished")
        .isTrue();
  }

}
