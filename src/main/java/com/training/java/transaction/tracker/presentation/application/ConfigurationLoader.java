package com.training.java.transaction.tracker.presentation.application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationLoader {

    private static ConfigurationLoader instance;

    private String connectionString;
    private String username;
    private String password;

    private ConfigurationLoader() {
        Properties properties = loadProperties();

        connectionString = properties.getProperty("connectionString");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    public static ConfigurationLoader getInstance() {
        if (instance == null)
            return new ConfigurationLoader();


        return instance;
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("config.properties");

            properties.load(inputStream);
            inputStream.close();
        } catch (IOException exception) {
            System.out.println("Unable to load properties");
        }

        return properties;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
