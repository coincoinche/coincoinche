package com.coincoinche.websockets.events;

public class SignUpEvent extends Event {
  private String username;
  private String password;

  public SignUpEvent() {
    super(EventType.SIGN_UP);
  }

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
