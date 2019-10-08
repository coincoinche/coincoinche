package com.coincoinche.websockets.messages;

public class HelloSocketMessage extends SocketMessage {

  private String content;
  private String from;

  /**
   * Sample websocket message.
   *
   * @param content - content of the message
   * @param from - who sends the message.
   * @param type - type of message.
   */
  public HelloSocketMessage(String content, String from, MessageType type) {
    super(type);
    this.content = content;
    this.from = from;
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
}
