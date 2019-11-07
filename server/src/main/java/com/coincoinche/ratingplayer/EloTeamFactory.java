package com.coincoinche.ratingplayer;

import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.coincoinche.model.User;
import com.coincoinche.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Factory to create instances of EloTeam. */
@Component
public class EloTeamFactory {

  @Autowired private UserRepository userRepository;

  public EloTeamFactory() {
    super();
  }

  /**
   * Create a new Elo team from a team from the coinche engine.
   *
   * @param team is the team from the coinche engine containing players.
   * @return the newly created Elo team.
   * @throws UserNotFoundException when a player was not found in the user table.
   */
  public EloTeam createEloTeamFromCoincheTeam(Team team) throws UserNotFoundException {
    List<EloPlayer> eloPlayers = new ArrayList<>();
    for (Player player : team.getPlayers()) {
      List<User> users = this.userRepository.findByUsername(player.getUsername());
      if (users.size() == 0) {
        throw new UserNotFoundException(player.getUsername());
      }
      eloPlayers.add(new EloPlayer(users.get(0)));
    }
    return new EloTeam(eloPlayers);
  }
}
