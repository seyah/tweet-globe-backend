package uk.co.seyah.tweetglobebackend.model;

public class Message {
  private String message;
  private String messageKey;
  private int status;

  public Message() {
  }

  public Message(String message, String messageKey, int status) {
    this.message = message;
    this.messageKey = messageKey;
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public String getMessageKey() {
    return messageKey;
  }
}