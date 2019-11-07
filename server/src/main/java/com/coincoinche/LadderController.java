package com.coincoinche;

import com.coincoinche.ratingplayer.EloPlayer;
import com.coincoinche.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller esponsible for providing the ladder (users and their rating). */
@RestController
public class LadderController {

  private static final Logger logger = LoggerFactory.getLogger(LadderController.class);
  private static final String DEFAULT_LADDER_SIZE = "100";
  @Autowired private UserRepository userRepository;

  /**
   * Endpoint to get the ladder of top players.
   *
   * @param size is the number of top players to display (default 100).
   * @return a sorted list of players.
   */
  @GetMapping("/ladder")
  public List<EloPlayer> getLadder(@RequestParam(defaultValue = DEFAULT_LADDER_SIZE) int size) {
    return userRepository.findAll().stream()
        .map(EloPlayer::new)
        .sorted((u1, u2) -> u2.getRating() - u1.getRating())
        .limit(size)
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
