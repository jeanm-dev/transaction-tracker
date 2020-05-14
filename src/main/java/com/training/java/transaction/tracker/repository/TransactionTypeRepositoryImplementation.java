package com.training.java.transaction.tracker.repository;

import com.training.java.transaction.tracker.data.Database;
import com.training.java.transaction.tracker.dao.TransactionType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionTypeRepositoryImplementation implements TransactionTypeRepository {

  private static final String TABLE_NAME = "TransactionTypes";
  private static final String TYPE_ID_COLUMN = "typeId";
  private static final String DESCRIPTION_COLUMN = "description";

  private static final String INSERT_TRANSACTION_TYPE_STATEMENT =
      "INSERT INTO " + TABLE_NAME + "(" + DESCRIPTION_COLUMN + ") VALUES (?);";
  private static final String DELETE_STATEMENT =
      "DELETE FROM " + TABLE_NAME + " WHERE " + DESCRIPTION_COLUMN + " = ?;";
  private static final String SELECT_ALL_STATEMENT =
      "SELECT " + TYPE_ID_COLUMN + ", " + DESCRIPTION_COLUMN + " FROM " + TABLE_NAME + ";";
  private static final String SELECT_TRANSACTION_BY_DESCRIPTION_STATEMENT =
      "SELECT " + TYPE_ID_COLUMN + ", " + DESCRIPTION_COLUMN + " FROM " + TABLE_NAME + " WHERE "
          + DESCRIPTION_COLUMN + " = ?;";
  private static final String UPDATE_STATEMENT =
      "UPDATE " + TABLE_NAME + " SET " + DESCRIPTION_COLUMN + " = ? WHERE " + TYPE_ID_COLUMN
          + " = ?;";

  private Database database;

  public TransactionTypeRepositoryImplementation(
      Database database) {
    this.database = database;
  }

  @Override
  public int create(String transactionType) throws SQLException {
    int transactionTypeId = -1; // Default values?
    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection
        .prepareStatement(INSERT_TRANSACTION_TYPE_STATEMENT, Statement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, transactionType);

    preparedStatement.execute();

    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

    if (generatedKeys.next()) {
      transactionTypeId = generatedKeys.getInt(1);
    }

    preparedStatement.close();

    return transactionTypeId;
  }

  @Override
  public boolean remove(String description) throws SQLException {
    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT);
    preparedStatement.setString(1, description);

    preparedStatement.execute();

    int updateCount = preparedStatement.getUpdateCount();

    preparedStatement.close();

    return updateCount > 0;
  }

  @Override
  public List<TransactionType> fetchAll() throws SQLException {
    List<TransactionType> transactionTypes = null;

    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STATEMENT);

    ResultSet resultSet = preparedStatement.executeQuery();

    while (resultSet.next()) {
      if (transactionTypes == null) {
        transactionTypes = new ArrayList<>();
      }

      TransactionType transactionType = mapToTransactionType(resultSet);
      transactionTypes.add(transactionType);
    }

    preparedStatement.close();

    return transactionTypes;
  }

  @Override
  public TransactionType fetchByDescription(String description) throws SQLException {
    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection
        .prepareStatement(SELECT_TRANSACTION_BY_DESCRIPTION_STATEMENT);
    preparedStatement.setString(1, description);

    ResultSet resultSet = preparedStatement.executeQuery();

    while (resultSet.next()) {
      return mapToTransactionType(resultSet);
    }

    preparedStatement.close();

    return null;
  }

  @Override
  public void update(TransactionType transactionType) throws SQLException {
    Connection connection = database.getConnection();

    PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATEMENT);
    preparedStatement.setString(1, transactionType.getDescription());
    preparedStatement.setInt(2, transactionType.getIdentifier());

    preparedStatement.execute();

    preparedStatement.close();
  }

  private TransactionType mapToTransactionType(ResultSet resultSet) throws SQLException {
    if (!resultSet.isClosed()) {
      int typeId;
      String description;

      typeId = resultSet.getInt(TYPE_ID_COLUMN);
      description = resultSet.getString(DESCRIPTION_COLUMN);

      return new TransactionType(typeId, description);
    }
    return null;
  }
}
