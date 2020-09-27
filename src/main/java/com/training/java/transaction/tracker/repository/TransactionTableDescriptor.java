package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.dao.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TransactionTableDescriptor implements
    TableDescriptor<Transaction> {

  private final List<String> columnNames = List
      .of("DESCRIPTION", "AMOUNT", "DATE_OF_TRANSACTION", "TRANSACTION_TYPE_ID");
  private final List<Function<Transaction, Object>> valueExtractors = List
      .of(Transaction::getDescription, Transaction::getAmount, Transaction::getDateOfTransaction,
          Transaction::getType);

  private final Map<String, Function<Transaction, Object>> columnValueMappers = Map
      .of("DESCRIPTION", Transaction::getDescription, "AMOUNT", Transaction::getAmount,
          "DATE_OF_TRANSACTION", Transaction::getDateOfTransaction, "TRANSACTION_TYPE_ID",
          Transaction::getType);

  @Override
  public String getTableName() {
    return "TransactionEntries";
  }

  @Override
  public String getIdentifierColumnName() {
    return "TRANSACTION_ID";
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
  public Function<Transaction, Long> getIdentifierExtractor() {
    return Transaction::getIdentifier;
  }

  @Override
  public BiConsumer<Long, Transaction> getIdentifierSetter() {
    return (Long l, Transaction t) -> t.setIdentifier(l);
  }

//  @Override
//  public List<Function<Transaction, Object>> getValueExtractors() {
//    return valueExtractors;
//  }

  @Override
  public Map<String, Function<Transaction, Object>> getColumnValueMappers() {
    return columnValueMappers;
  }
}
