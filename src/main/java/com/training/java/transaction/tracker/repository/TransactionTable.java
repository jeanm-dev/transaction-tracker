package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.dao.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

// TODO: Consider moving to table descriptor constructor with properties
public class TransactionTable implements TableDescriptor<Transaction> {

  private final List<String> columnNames = List
      .of("DESCRIPTION", "AMOUNT", "DATE_OF_TRANSACTION", "TRANSACTION_TYPE_ID");
  private final List<Function<Transaction, Object>> valueExtractors = List
      .of(Transaction::getDescription, Transaction::getAmount, Transaction::getDateOfTransaction,
          Transaction::getType);

  @Override
  public String getTableName() {
    return "TransactionEntries";
  }

  @Override
  public String getIdentifierColumnName() {
    return "transactionId";
  }

  @Override
  public List<String> getColumnNames() {
    return columnNames;
  }

  @Override
  public List<Class<?>> getColumnTypes() {
    return List.of(String.class, BigDecimal.class, Date.class, Integer.class);
  }

  @Override
  public List<Function<Transaction, Object>> getValueExtractors() {
    return valueExtractors;
  }
}
