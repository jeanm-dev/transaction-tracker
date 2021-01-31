package com.training.java.transaction.tracker.service.request;

public class DeleteTransactionRequest {

  private final Long transactionId;

  public DeleteTransactionRequest(Long transactionId) {
    this.transactionId = transactionId;
  }

  public Long getTransactionId() {
    return transactionId;
  }
}
