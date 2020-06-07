package com.training.java.transaction.tracker.service.request;

import java.math.BigDecimal;
import java.util.Date;

public class UpdateTransactionRequest {

  private final int transactionId;
  private final String description;
  private final BigDecimal amount;
  private final Date dateOfTransaction;

  public UpdateTransactionRequest(int transactionId, String description, BigDecimal amount,
      Date dateOfTransaction) {
    this.transactionId = transactionId;
    this.description = description;
    this.amount = amount;
    this.dateOfTransaction = dateOfTransaction;
  }

  public int getTransactionId() {
    return transactionId;
  }

  public String getDescription() {
    return description;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Date getDateOfTransaction() {
    return dateOfTransaction;
  }
}
