package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RepositoryBase<T extends TableDescriptor.TableRow, D extends TableDescriptor<T>> implements Repository<T> {

  private final Database database;
  private final D tableDescriptor;

  private static final String ADD_STATEMENT = "INSERT INTO {TABLE_NAME} ({ALL_COLUMNS}) VALUES ({VALUES_STRING})";

  public RepositoryBase(Database database, D tableDescriptor) {
    this.database = database;
    this.tableDescriptor = tableDescriptor;
  }

  //TODO: Construct these using table descriptor
//  getWhereClause();
//  getInsertStatement();


  @Override
  public T create(T object) throws SQLException {
    Connection connection = database.getConnection();

    //TODO: Replace ADD_STATEMENT with constructed prepare statement using TableDescriptor and TableRow
    PreparedStatement preparedStatement = connection.prepareStatement(ADD_STATEMENT, Statement.RETURN_GENERATED_KEYS);
    // TODO: Set parameters using TableDescriptor
//    preparedStatement.setObject();
    preparedStatement.execute();

    ResultSet resultSet = preparedStatement.getGeneratedKeys();
    if (resultSet.next()) {
      int identifier = resultSet.getInt(1);
      object.setIdentifier(identifier);
    }

    preparedStatement.close();

    return object;
  }

  @Override
  public boolean remove(int id) throws SQLException {
    return false;
  }

  @Override
  public void update(T object) throws SQLException {

  }

  @Override
  public List<T> fetchAll() throws SQLException {
    return null;
  }

  @Override
  public boolean doesIdExist(int id) throws SQLException {
    return false;
  }

  @Override
  public T fetchById(int id) throws SQLException {
    return null;
  }
}
