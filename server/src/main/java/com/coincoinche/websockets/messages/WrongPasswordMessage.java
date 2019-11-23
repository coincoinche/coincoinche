package com.coincoinche.websockets.messages;

/** Message sent when the entered username and password don't match. */
public class WrongPasswordMessage extends Message {

  @Override
  public String getMessageType() {
    return "WRONG_PASSWORD";
  }

@Override
  public String getJsonContent() {
    return "{}";
  }
}
