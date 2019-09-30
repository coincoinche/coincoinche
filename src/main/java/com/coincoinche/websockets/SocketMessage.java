package com.coincoinche.websockets;

public class SocketMessage {

  private String content;
  private String from;
  private String type;

  public SocketMessage(String content, String from, String type) {
    this.content = content;
    this.from = from;
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
