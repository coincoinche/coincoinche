package com.coincoinche.ratingplayer;

import com.coincoinche.engine.teams.Team;
import com.coincoinche.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Computation of Elo rating for players. */
@Service
public class EloService {

  @Autowired private UserRepository userRepository;
  @Autowired EloTeamFactory eloTeamFactory;

  private static final Logger logger = LoggerFactory.getLogger(EloService.class);

  public EloService() {
    super();
  }

  /**
   * Update ratings of users from the provided coinche teams.
   *
   * @param winnerTeam is the coinche team winning the game.
   * @param loserTeam is the coinche team losing the game.
   */
  public void updateRatings(Team winnerTeam, Team loserTeam) {
    try {
      EloTeam winner = eloTeamFactory.createEloTeamFromCoincheTeam(winnerTeam);
      EloTeam loser = eloTeamFactory.createEloTeamFromCoincheTeam(loserTeam);
      updateEloTeamRatings(winner, loser);
      logger.debug("Successfully updated Elo ratings");
    } catch (Exception e) {
      logger.error(String.format("{}: skip Elo ratings update", e));
    }
  }

  private void updateEloTeamRatings(EloTeam winner, EloTeam loser) {
    int ratingWinner = winner.getRating();
    int ratingLoser = loser.getRating();
    double quotientWinner = Math.pow(10, ratingWinner / 400);
    double quotientLoser = Math.pow(10, ratingLoser / 400);
    double expectedWinner = quotientWinner / (quotientWinner + quotientLoser);
    double expectedLoser = quotientLoser / (quotientWinner + quotientLoser);
    for (EloPlayer player : winner.getPlayers()) {
      player.setRating(
          (int)
              Math.round(player.getRating() + player.getRatingAdjustment() * (1 - expectedWinner)));
      userRepository.save(player.getUser());
    }
    for (EloPlayer player : loser.getPlayers()) {
      player.setRating(
          (int)
              Math.round(player.getRating() + player.getRatingAdjustment() * (0 - expectedLoser)));
      userRepository.save(player.getUser());
    }
  }
}
