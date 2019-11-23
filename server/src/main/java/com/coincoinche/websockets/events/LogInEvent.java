package com.coincoinche.websockets.events;

public class LogInEvent extends Event {
  private String username;
  private String password;

  public LogInEvent() {
    super(EventType.LOG_IN);
  }

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
