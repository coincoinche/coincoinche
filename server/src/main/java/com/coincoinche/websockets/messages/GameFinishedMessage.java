package com.coincoinche.websockets.messages;

import java.util.Map;

/** Message sent when the game has finished. */
public class GameFinishedMessage extends Message {

  private boolean win;
  private int yourTeamPoints;
  private int otherTeamPoints;
  private Map<String, Integer> newEloPerUsername;

  /**
   * Create a message to notify the game ending.
   *
   * @param win is a boolean indicating if the message is for a winning team.
   */
  public GameFinishedMessage(
      boolean win,
      int yourTeamPoints,
      int otherTeamPoints,
      Map<String, Integer> newEloPerUsername) {
    super();
    this.win = win;
    this.yourTeamPoints = yourTeamPoints;
    this.otherTeamPoints = otherTeamPoints;
    this.newEloPerUsername = newEloPerUsername;
  }

  @Override
  public String getMessageType() {
    return "GAME_FINISHED";
  }

  @Override
  public String getJsonContent() {
    String baseJsonContent =
        String.format(
            "\"win\":%s, \"yourTeamPoints\":%s, \"otherTeamPoints\":%s",
            win, yourTeamPoints, otherTeamPoints);
    StringBuffer eloUpdateJsonContent = new StringBuffer();
    int usernamesAdded = 0;
    for (String username : newEloPerUsername.keySet()) {
      eloUpdateJsonContent.append(
          String.format("\"%s\": %s", username, newEloPerUsername.get(username)));
      usernamesAdded += 1;
      if (usernamesAdded < 4) {
        eloUpdateJsonContent.append(",");
      }
    }
    return String.format(
        "{%s, \"eloUpdate\": {%s}}", baseJsonContent, eloUpdateJsonContent.toString());
  }
}
