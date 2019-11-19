package com.coincoinche.websockets.messages;

/** Message sent to the client when some operation was successful. */
public class SuccessMessage extends Message {

  @Override
  public String getMessageType() {
    return "SUCCESS";
  }

  @Override
  public String getJsonContent() {
    return "{}";
  }
}
