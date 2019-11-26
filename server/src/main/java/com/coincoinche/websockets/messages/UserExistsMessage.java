package com.coincoinche.websockets.messages;

/** Message sent when the entered username is already taken. */
public class UserExistsMessage extends Message {

  @Override
  public String getMessageType() {
    return "USER_EXISTS";
  }

  @Override
  public String getJsonContent() {
    return "{}";
  }
}
