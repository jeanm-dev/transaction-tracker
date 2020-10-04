package com.training.java.transaction.tracker.repository;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ColumnDescriptor<T, S> {

  private final String name;
  private final Function<T, S> valueExtractor;
  private final BiConsumer<T, S> valueSetter;
  private final Boolean isRequired;
  private S type;

  public ColumnDescriptor(String name, Function<T, S> valueExtractor, BiConsumer<T, S> valueSetter,
      Boolean isRequired) {
    this.name = name;
    this.valueExtractor = valueExtractor;
    this.valueSetter = valueSetter;
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

  public BiConsumer<T, S> getValueSetter() {
    return valueSetter;
  }
}
