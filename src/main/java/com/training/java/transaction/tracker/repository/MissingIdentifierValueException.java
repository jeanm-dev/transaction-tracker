package com.training.java.transaction.tracker.repository;

public class MissingIdentifierValueException extends Exception {
  public MissingIdentifierValueException() {
    super("The object is missing the identifier");
  }
}
