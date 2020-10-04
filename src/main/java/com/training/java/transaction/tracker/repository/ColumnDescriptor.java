package com.training.java.transaction.tracker.repository;

import java.util.function.Function;

public class ColumnDescriptor<T, S> {

  private String name;
  private Function<T, S> valueExtractor;
  private Boolean isRequired;
  private S type;

  public ColumnDescriptor(String name, Function<T, S> valueExtractor, Boolean isRequired) {
    this.name = name;
    this.valueExtractor = valueExtractor;
    this.isRequired = isRequired;
  }

  public String getName() {
    return name;
  }

  public Function<T, S> getValueExtractor() {
    return valueExtractor;
  }

  public Boolean getRequired() {
    return isRequired;
  }

  public Class<?> getType() {
    return type.getClass();
  }
}
