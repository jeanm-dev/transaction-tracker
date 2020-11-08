package com.training.java.transaction.tracker.repository;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface TableDescriptor<T> {


  String getTableName();

  String getIdentifierColumnName();

  List<String> getColumnNames();

  Map<String, Boolean> getRequiredColumnNames();

  Map<String, Class<?>> getColumnTypes();

  Function<T, Long> getIdentifierExtractor();

  BiConsumer<T, Long> getIdentifierSetter();

  Map<String, Function<T, ?>> getColumnValueMappers();

  T newObject();

  Map<String, BiConsumer<T, Object>> getColumnSetters();
}