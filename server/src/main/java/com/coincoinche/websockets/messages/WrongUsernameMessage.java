package com.coincoinche.websockets.messages;

/** Message sent when the entered username doesn't exist. */
public class WrongUsernameMessage extends Message {

  @Override
  public String getMessageType() {
    return "WRONG_USERNAME";
  }

@Override
  public String getJsonContent() {
    return "{}";
  }
}
