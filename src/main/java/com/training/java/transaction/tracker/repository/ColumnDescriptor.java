package com.training.java.transaction.tracker.repository;

import java.util.function.Function;

public class ColumnDescriptor<T, S> {

  private final String name;
  private final Function<T, S> valueExtractor;
  private final Boolean isRequired;
  private S type;

  public ColumnDescriptor(String name, Function<T, S> valueExtractor, Boolean isRequired, S type) {
    this.name = name;
    this.valueExtractor = valueExtractor;
    this.isRequired = isRequired;
    this.type = type;
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
