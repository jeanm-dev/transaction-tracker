package com.training.java.transaction.tracker.service.dto;

import com.training.java.transaction.tracker.dao.Transaction;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionDto {

  private final int identifier;
  private final String description;
  private final BigDecimal amount;
  private final Date dateOfTransaction;
  private final String type;

  public TransactionDto(int identifier, String description, BigDecimal amount,
      Date dateOfTransaction, String type) {
    this.identifier = identifier;
    this.description = description;
    this.amount = amount;
    this.dateOfTransaction = dateOfTransaction;
    this.type = type;
  }

  public TransactionDto(Transaction transaction, String type) {
    this.identifier = transaction.getIdentifier();
    this.description = transaction.getDescription();
    this.amount = transaction.getAmount();
    this.dateOfTransaction = transaction.getDateOfTransaction();
    this.type = type;
  }

  public int getIdentifier() {
    return identifier;
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

  public String getType() {
    return type;
  }
}
