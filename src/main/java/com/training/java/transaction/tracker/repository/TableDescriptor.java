package com.training.java.transaction.tracker.repository;

import java.util.List;
import java.util.function.Function;

public interface TableDescriptor<R extends TableDescriptor.TableRow> {

  interface TableRow {

    long getIdentifier();

    void setIdentifier(long identifier);

    List<Object> getColumnValues(); // TODO: Investigate alternative - Lambdas

    <T> T getValueForColumn(String columnName);
  }

  String getTableName();

  String getIdentifierColumnName();

  List<String> getColumnNames();

  List<Class<?>> getColumnTypes();



  List<Function<R, Object>> getValueExtractors();
}