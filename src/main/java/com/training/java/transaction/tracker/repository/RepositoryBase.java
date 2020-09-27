package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

//public class RepositoryBase<T extends TableDescriptor.TableRow<V>, D extends TableDescriptor<T, V>, V> implements
public class RepositoryBase<T, D extends TableDescriptor<T>> implements Repository<T> {

  private final Database database;
  private final D tableDescriptor;

  private static final String ADD_STATEMENT = "INSERT INTO {TABLE_NAME} ({ALL_COLUMNS}) VALUES ({VALUES_STRING})";

  public RepositoryBase(Database database, D tableDescriptor) {
    this.database = database;
    this.tableDescriptor = tableDescriptor;
  }

  //TODO: Construct these using table descriptor
  //  getWhereClause();


  private String getInsertStatement() {
    String columnNames = String.join(",", tableDescriptor.getColumnNames());
    String values = tableDescriptor.getColumnNames().stream().map( l -> "?").collect(Collectors.joining(","));

    return ADD_STATEMENT
        .replace("{TABLE_NAME}", tableDescriptor.getTableName())
        .replace("{ALL_COLUMNS}", columnNames)
        .replace("{VALUES_STRING}", values);
  }


  @Override
  public T create(T object) throws Exception {
    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection
        .prepareStatement(getInsertStatement(), Statement.RETURN_GENERATED_KEYS);

    for (int i = 0; i < tableDescriptor.getColumnNames().size(); i++) {
      Object insertObject = tableDescriptor.getColumnValueMappers().get(tableDescriptor.getColumnNames().get(i)).apply(object);
      if (insertObject != null) {
        preparedStatement.setObject(i + 1, insertObject);
      } else {
        throw new MissingFieldValueException(tableDescriptor.getColumnNames().get(i));
      }
    }
    preparedStatement.execute();

    ResultSet resultSet = preparedStatement.getGeneratedKeys();
    if (resultSet.next()) {
      long identifier = resultSet.getLong(1);
      tableDescriptor.getIdentifierSetter().accept(identifier,object);
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

  @Override
  public boolean isValid(T object) {
    boolean validResult = true;
    for(String columnName: tableDescriptor.getColumnNames()) {
      validResult = tableDescriptor.getColumnValueMappers().get(columnName).apply(object) != null;
    }
    return validResult;
  }
}
