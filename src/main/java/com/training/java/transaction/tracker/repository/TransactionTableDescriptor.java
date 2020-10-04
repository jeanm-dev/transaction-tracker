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
      new ColumnDescriptor<Transaction, String>("DESCRIPTION", Transaction::getDescription, true),
      new ColumnDescriptor<Transaction, BigDecimal>("AMOUNT", Transaction::getAmount, true),
      new ColumnDescriptor<Transaction, Date>("DATE_OF_TRANSACTION",
          Transaction::getDateOfTransaction, true),
      new ColumnDescriptor<Transaction, Integer>("TRANSACTION_TYPE_ID", Transaction::getType, false)
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
  public List<String> getRequiredColumnNames() {
    return columnDescriptors.stream().map(ColumnDescriptor::getName).collect(Collectors.toList());
  }

  @Override
  public List<Class<?>> getColumnTypes() {
    return columnDescriptors.stream().map(ColumnDescriptor::getType).collect(Collectors.toList());
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
  public BiConsumer<Long, Transaction> getIdentifierSetter() {
    return (Long l, Transaction t) -> t.setIdentifier(l);
  }

  @Override
  public Map<String, Function<Transaction, ?>> getColumnValueMappers() {
    return columnDescriptors.stream()
        .collect(Collectors.toMap(ColumnDescriptor::getName, ColumnDescriptor::getValueExtractor));
  }
}
