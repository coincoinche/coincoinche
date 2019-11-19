package com.coincoinche.websockets.messages;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;

/** Message sent to the client via websocket when the game is played. */
public abstract class Message {

  public abstract String getMessageType();

  public abstract String getJsonContent();

  @JsonValue
  @JsonRawValue
  public String toJson() {
    return String.format("{\"message\":\"%s\",\"content\":%s}", getMessageType(), getJsonContent());
  }
}
