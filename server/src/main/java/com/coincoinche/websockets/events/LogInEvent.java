package com.coincoinche.websockets.events;

/** Event received when a user logs in. */
public class LogInEvent extends Event {
  private String username;
  private String password;

  public LogInEvent() {
    super(EventType.LOG_IN);
  }

  /**
   * Create a new log in event with a username and password.
   *
   * @param username is the username of the user.
   * @param password is their password.
   */
  public LogInEvent(String username, String password) {
    super(EventType.LOG_IN);
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
