package online_course_registration_system.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:oracle:thin:@localhost:1521:xe");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "system");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    private DatabaseConnection() {
        if (DB_PASSWORD == null || DB_PASSWORD.isBlank()) {
            throw new IllegalStateException("Set DB_PASSWORD environment variable before running the application.");
        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}