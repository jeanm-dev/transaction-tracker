package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.dao.Transaction;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface TableDescriptor<T> {


  String getTableName();

  String getIdentifierColumnName();

  List<String> getColumnNames();

  List<Class<?>> getColumnTypes();

  Function<T, Long> getIdentifierExtractor();
  BiConsumer<Long, T> getIdentifierSetter();

  Map<String, Function<T, Object>> getColumnValueMappers();
}