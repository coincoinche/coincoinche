package com.coincoinche.websockets.events;

/** Event received when the user signs up. */
public class SignUpEvent extends Event {
  private String username;
  private String password;

  public SignUpEvent() {
    super(EventType.SIGN_UP);
  }

  /**
   * Create a new sign-up event for a user.
   *
   * @param username is the username of the user.
   * @param password is their password.
   */
  public SignUpEvent(String username, String password) {
    super(EventType.SIGN_UP);
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
