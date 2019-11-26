package com.coincoinche.websockets.messages;

/** Message sent when a user is created. */
public class UserCreatedMessage extends Message {

  @Override
  public String getMessageType() {
    return "USER_CREATED";
  }

  @Override
  public String getJsonContent() {
    return "{}";
  }
}
