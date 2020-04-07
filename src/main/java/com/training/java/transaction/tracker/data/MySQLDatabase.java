package com.training.java.transaction.tracker.data;


import javafx.util.Pair;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase implements Database {

    private String connectionString;
    private String username;
    private String password;

    private Connection connection;

    public MySQLDatabase(String connectionString, String username, String password) {
        this.connectionString = connectionString;
        this.username = username;
        this.password = password;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, username, password);
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) this.connection = createConnection();
        return connection;
    }
}
