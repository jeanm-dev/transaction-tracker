package com.training.java.transaction.tracker.data;

import java.sql.Connection;
import java.sql.SQLException;


public interface Database {

    Connection getConnection() throws SQLException;
}
