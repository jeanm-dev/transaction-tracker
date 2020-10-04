package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryBase<T, D extends TableDescriptor<T>> implements Repository<T> {

  private final Database database;
  private final D tableDescriptor;

  private static final String ADD_STATEMENT = "INSERT INTO {TABLE_NAME} ({ALL_COLUMNS}) VALUES ({VALUES_STRING});";
  private static final String SELECT_WHERE_STATEMENT = "SELECT {ALL_COLUMNS} FROM {TABLE_NAME} WHERE {ID_COLUMN} = {ID_VALUE};";
  private static final String DELETE_STATEMENT = "DELETE FROM {TABLE_NAME} WHERE {ID_COLUMN} = ?;";

  public RepositoryBase(Database database, D tableDescriptor) {
    this.database = database;
    this.tableDescriptor = tableDescriptor;
  }

  private String getInsertStatement() {
    String columnNames = String.join(",", tableDescriptor.getColumnNames());
    String values = tableDescriptor.getColumnNames().stream().map(l -> "?")
        .collect(Collectors.joining(","));

    return ADD_STATEMENT
        .replace("{TABLE_NAME}", tableDescriptor.getTableName())
        .replace("{ALL_COLUMNS}", columnNames)
        .replace("{VALUES_STRING}", values);
  }

  private String getWhereClause() {
    String columnNames = String.join(",", tableDescriptor.getColumnNames());

    return SELECT_WHERE_STATEMENT
        .replace("{TABLE_NAME}", tableDescriptor.getTableName())
        .replace("{ALL_COLUMNS}", columnNames)
        .replace("{ID_COLUMN}", tableDescriptor.getIdentifierColumnName())
        .replace("{ID_VALUE}", "?");
  }

  private String getDeleteStatement() {
    return DELETE_STATEMENT
        .replace("{TABLE_NAME}", tableDescriptor.getTableName())
        .replace("{ID_COLUMN}", tableDescriptor.getIdentifierColumnName());
  }

  @Override
  public T create(T object) throws Exception {
    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection
        .prepareStatement(getInsertStatement(), Statement.RETURN_GENERATED_KEYS);

    for (int i = 0; i < tableDescriptor.getColumnNames().size(); i++) {
      String columnName = tableDescriptor.getColumnNames().get(i);

      Object insertObject = tableDescriptor.getColumnValueMappers().get(columnName).apply(object);
      if (insertObject != null) {
        preparedStatement.setObject(i + 1, insertObject);
      } else if (tableDescriptor.getRequiredColumnNames().get(columnName)) {
        throw new MissingFieldValueException(columnName);
      } else {
        preparedStatement.setObject(i + 1, null);
      }
    }
    preparedStatement.execute();

    ResultSet resultSet = preparedStatement.getGeneratedKeys();
    if (resultSet.next()) {
      long identifier = resultSet.getLong(1);
      tableDescriptor.getIdentifierSetter().accept(identifier, object);
    }

    preparedStatement.close();

    return object;
  }

  @Override
  public boolean doesIdExist(long id) throws SQLException {
    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection.prepareStatement(getWhereClause());
    preparedStatement.setLong(1, id);

    System.out.println(preparedStatement);
    ResultSet resultSet = preparedStatement.executeQuery();

    while (resultSet.next()) {
      return true;
    }

    preparedStatement.close();

    return false;
  }

  @Override
  public T fetchById(long id) throws SQLException {
    return null;
  }

  @Override
  public boolean remove(long id) throws SQLException {
    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection.prepareStatement(getDeleteStatement());
    preparedStatement.setLong(1, id);

    preparedStatement.execute();

    // Determine if changes where applied
    int updateCount = preparedStatement.getUpdateCount();

    preparedStatement.close();

    return updateCount > 0;
  }

  @Override
  public void update(T object) throws SQLException {

  }

  @Override
  public List<T> fetchAll() throws SQLException {
    return null;
  }

  @Override
  public boolean isValid(T object) {
    for (String columnName : tableDescriptor.getRequiredColumnNames().keySet()) {
      if (tableDescriptor.getColumnValueMappers().get(columnName).apply(object) == null) {
        return false;
      }
    }
    return true;
  }
}
