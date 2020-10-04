package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.dao.Transaction;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionTableDescriptor implements
    TableDescriptor<Transaction> {

  private final List<ColumnDescriptor<Transaction, ?>> columnDescriptors = List.of(
      new ColumnDescriptor<>("DESCRIPTION", Transaction::getDescription,
          Transaction::setDescription, true),
      new ColumnDescriptor<>("AMOUNT", Transaction::getAmount, Transaction::setAmount, true),
      new ColumnDescriptor<>("DATE_OF_TRANSACTION", Transaction::getDateOfTransaction,
          Transaction::setDateOfTransaction, true),
      new ColumnDescriptor<>("TRANSACTION_TYPE_ID", Transaction::getType, Transaction::setType,
          false)
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

  //TODO: Throws here?
  @Override
  public Map<String, BiConsumer<Transaction, ?>> getColumnSetters() {
    return columnDescriptors.stream()
        .collect(Collectors.toMap(ColumnDescriptor::getName, ColumnDescriptor::getValueSetter));
  }

//  @Override
//  public Map<String, BiConsumer<Transaction, Object>> getColumnSetters() {
//    return columnDescriptors.stream()
//        .collect(Collectors.toMap(ColumnDescriptor::getName, ColumnDescriptor::getValueSetter));
//  }
}
