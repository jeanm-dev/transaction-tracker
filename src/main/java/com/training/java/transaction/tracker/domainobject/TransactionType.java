package com.training.java.transaction.tracker.domainobject;

public final class TransactionType {

  private int identifier;
  private String description;

  public TransactionType(int identifier, String description) {
    this.identifier = identifier;
    this.description = description;
  }

  public int getIdentifier() {
    return identifier;
  }

  public String getDescription() {
    return description;
  }
}
