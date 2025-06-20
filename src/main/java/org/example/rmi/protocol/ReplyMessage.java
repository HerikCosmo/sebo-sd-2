package org.example.rmi.protocol;

import java.io.Serializable;

public class ReplyMessage implements Serializable {
  private static final long serialVersionUID = 1L;

  private int messageType;
  private int requestId;
  private byte[] result;
  private String status;
  private String errorMessage;

  public ReplyMessage() {
    this.messageType = 1;
  }

  public ReplyMessage(int requestId, String status, byte[] result) {
    this.messageType = 1;
    this.requestId = requestId;
    this.status = status;
    this.result = result;
    this.errorMessage = null;
  }

  public ReplyMessage(int requestId, String status, String errorMessage) {
    this.messageType = 1;
    this.requestId = requestId;
    this.status = status;
    this.result = null;
    this.errorMessage = errorMessage;
  }

  public int getMessageType() {
    return messageType;
  }

  public void setMessageType(int messageType) {
    this.messageType = messageType;
  }

  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public byte[] getResult() {
    return result;
  }

  public void setResult(byte[] result) {
    this.result = result;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
