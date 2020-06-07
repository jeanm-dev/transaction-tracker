package com.training.java.transaction.tracker.service.request;

public class DeleteTransactionRequest {

  private final int transactionId;

  public DeleteTransactionRequest(int transactionId) {
    this.transactionId = transactionId;
  }

  public int getTransactionId() {
    return transactionId;
  }
}
