package com.coincoinche.websockets.messages;

/** Message sent when the client sends an invalied event. */
public class InvalidEventMessage extends Message {

  @Override
  public String getMessageType() {
    return "INVALID_EVENT";
  }

  @Override
  public String getJsonContent() {
    return "{}";
  }
}
