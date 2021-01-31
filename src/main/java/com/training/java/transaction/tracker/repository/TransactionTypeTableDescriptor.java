package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.dao.Transaction;
import com.training.java.transaction.tracker.dao.TransactionType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionTypeTableDescriptor implements TableDescriptor<TransactionType> {

  private final List<ColumnDescriptor<TransactionType, ?>> columnDescriptors = List.of(
      new ColumnDescriptor<>("DESCRIPTION", TransactionType::getDescription, true, String.class)
  );

  @Override
  public String getTableName() {
    return "TransactionTypes";
  }

  @Override
  public String getIdentifierColumnName() {
    return "TYPE_ID";
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
  public Function<TransactionType, Long> getIdentifierExtractor() {
    return TransactionType::getIdentifier;
  }

  @Override
  public BiConsumer<TransactionType, Long> getIdentifierSetter() {
    return TransactionType::setIdentifier;
  }

  @Override
  public Map<String, Function<TransactionType, ?>> getColumnValueMappers() {
    return columnDescriptors.stream()
        .collect(Collectors.toMap(ColumnDescriptor::getName, ColumnDescriptor::getValueExtractor));
  }

  @Override
  public TransactionType newObject() {
    return new TransactionType();
  }

  @Override
  public Map<String, BiConsumer<TransactionType, Object>> getColumnSetters() {
    return Map.of(
        "DESCRIPTION", (transactionType, object) -> transactionType.setDescription((String) object)
    );
  }
}
