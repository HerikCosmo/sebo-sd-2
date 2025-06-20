package org.example.rmi.protocol;

import java.io.Serializable;

public class RequestMessage implements Serializable {
  private static final long serialVersionUID = 1L;

  private final int messageType;
  private int requestId;
  private String objectReference;
  private String methodId;
  private byte[] arguments;

  public RequestMessage() {
    this.messageType = 0;
  }

  public RequestMessage(int requestId, String objectReference, String methodId, byte[] arguments) {
    this.messageType = 0; // Request
    this.requestId = requestId;
    this.objectReference = objectReference;
    this.methodId = methodId;
    this.arguments = arguments;
  }

  public int getMessageType() {
    return messageType;
  }

  public int getRequestId() {
    return requestId;
  }

  public String getObjectReference() {
    return objectReference;
  }

  public String getMethodId() {
    return methodId;
  }

  public byte[] getArguments() {
    return arguments;
  }
}
