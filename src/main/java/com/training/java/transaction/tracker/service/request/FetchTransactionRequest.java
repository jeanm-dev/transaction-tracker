package com.training.java.transaction.tracker.service.request;

public class FetchTransactionRequest {

  private final Long transactionId;

  public FetchTransactionRequest(Long transactionId) {
    this.transactionId = transactionId;
  }

  public Long getTransactionId() {
    return transactionId;
  }
}
