package com.training.java.transaction.tracker.dao;

public final class TransactionType {

  private Long identifier;

  private String description;

  public TransactionType () {}

  public TransactionType(Long identifier, String description) {
    this.identifier = identifier;
    this.description = description;
  }

  public Long getIdentifier() {
    return identifier;
  }

  public String getDescription() {
    return description;
  }

  public void setIdentifier(Long identifier) {
    this.identifier = identifier;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
