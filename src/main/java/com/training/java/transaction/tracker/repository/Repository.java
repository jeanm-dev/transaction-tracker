package com.training.java.transaction.tracker.repository;

import java.sql.SQLException;
import java.util.List;

//public interface Repository<T extends TableDescriptor.TableRow> {
public interface Repository<T> {
  T create(T object) throws Exception;

  boolean remove(int id) throws SQLException;

  void update(T object) throws SQLException;

  List<T> fetchAll() throws SQLException;

  boolean doesIdExist(int id) throws SQLException;

  T fetchById(int id) throws SQLException;

  boolean isValid(T object);
}
