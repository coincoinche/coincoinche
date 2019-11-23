package com.coincoinche.websockets.messages;

/** Message sent when the entered username and password don't match. */
public class LoggedInMessage extends Message {

  @Override
  public String getMessageType() {
    return "LOGGED_IN";
  }

@Override
  public String getJsonContent() {
    return "{}";
  }
}
