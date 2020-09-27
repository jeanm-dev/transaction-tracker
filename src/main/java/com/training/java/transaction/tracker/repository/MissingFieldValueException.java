package com.training.java.transaction.tracker.repository;

public class MissingFieldValueException extends Exception {
  public MissingFieldValueException(String columnName) {
    super("The object is missing a value for the column named:" + columnName);
  }
}
