package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.dao.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionTableDescriptor implements
    TableDescriptor<Transaction> {

  private final List<ColumnDescriptor<Transaction, ?>> columnDescriptors = List.of(
      new ColumnDescriptor<>("DESCRIPTION", Transaction::getDescription, true),
      new ColumnDescriptor<>("AMOUNT", Transaction::getAmount, true),
      new ColumnDescriptor<>("DATE_OF_TRANSACTION", Transaction::getDateOfTransaction, true),
      new ColumnDescriptor<>("TRANSACTION_TYPE_ID", Transaction::getType, false)
  );

  @Override
  public String getTableName() {
    return "TransactionEntries";
  }

  @Override
  public List<String> getColumnNames() {
    return columnDescriptors.stream().map(ColumnDescriptor::getName).collect(Collectors.toList());
  }

  @Override
  public Map<String, Boolean> getRequiredColumnNames() {
    return columnDescriptors.stream()
        .collect(Collectors.toMap(ColumnDescriptor::getName, ColumnDescriptor::getRequired));
  }

  @Override
  public Map<String, Class<?>> getColumnTypes() {
    return columnDescriptors.stream()
        .collect(Collectors.toMap(ColumnDescriptor::getName, ColumnDescriptor::getType));
  }

  @Override
  public String getIdentifierColumnName() {
    return "TRANSACTION_ID";
  }

  @Override
  public Function<Transaction, Long> getIdentifierExtractor() {
    return Transaction::getIdentifier;
  }

  @Override
  public BiConsumer<Transaction, Long> getIdentifierSetter() {
    return Transaction::setIdentifier;
  }

  @Override
  public Transaction newObject() {
    return new Transaction();
  }

  @Override
  public Map<String, Function<Transaction, ?>> getColumnValueMappers() {
    return columnDescriptors.stream()
        .collect(Collectors.toMap(ColumnDescriptor::getName, ColumnDescriptor::getValueExtractor));
  }

  @Override
  public Map<String, BiConsumer<Transaction, Object>> getColumnSetters() {
    //TODO: See if this can be moved to the ColumnDescriptor class
    return Map.of(
        "DESCRIPTION", (transaction, object) -> transaction.setDescription((String) object),
        "AMOUNT", (transaction, object) -> transaction.setAmount((BigDecimal) object),
        "DATE_OF_TRANSACTION",
        (transaction, object) -> transaction.setDateOfTransaction((Date) object),
        "TRANSACTION_TYPE_ID", (transaction, object) -> transaction.setType((Integer) object)
    );
  }
}
