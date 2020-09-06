package com.training.java.transaction.tracker.dao;

import com.training.java.transaction.tracker.repository.TableDescriptor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class Transaction implements TableDescriptor.TableRow {

  private long identifier;
  private String description;
  private BigDecimal amount;
  private Date dateOfTransaction;
  private Integer type; // Null? invalid value

  public Transaction() {
  }

  public Transaction(String description, BigDecimal amount, Date dateOfTransaction) {
    this.description = description;
    this.amount = amount;
    this.dateOfTransaction = dateOfTransaction;
  }

  @Override
  public long getIdentifier() {
    return identifier;
  }

  @Override
  public void setIdentifier(long identifier) {
    this.identifier = identifier;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }


  public Date getDateOfTransaction() {
    return dateOfTransaction;
  }

  public void setDateOfTransaction(Date dateOfTransaction) {
    this.dateOfTransaction = dateOfTransaction;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Override
  public List<Object> getColumnValues() {
    return List.of(description, amount, dateOfTransaction, type);
  }

  @Override
  public <T> T getValueForColumn(String columnName) {
    switch (columnName) {
      //TODO: Map Identifier Investigate
      case "DESCRIPTION":
        return (T) description;
      case "AMOUNT":
        return (T) amount;
      case "DATE_OF_TRANSACTION":
        return (T) dateOfTransaction;
      case "TRANSACTION_TYPE_ID":
        return (T) type;
      default:
        // Should only use during
        throw new IllegalArgumentException("Invalid columnName: " + columnName);
    }
  }
}
