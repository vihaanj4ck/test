package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static DBConnector.DB_URL;
import static DBConnector.DB_URL;
import static DBConnector.PASS;
import static DBConnector.USER;

public class DBConnector {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster_db";
    private static final String USER = "root";
    private static final String PASS = "guviproject";

    // Retry configuration (Step 5.3)
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;

    /**
     * Basic connection method (original)
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Enhanced connection with retry logic (Step 5.3)
     */
    public static Connection getConnectionWithRetry() throws SQLException {
        SQLException lastError = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("✅ Connection established on attempt " + attempt);
                return conn;
            } catch (SQLException e) {
                lastError = e;
                System.err.printf("⚠ Connection failed (attempt %d/%d): %s\n",
                        attempt, MAX_RETRIES, e.getMessage());

                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Connection interrupted", ie);
                    }
                }
            }
        }
        throw new SQLException("Failed to connect after " + MAX_RETRIES + " attempts", lastError);
    }

    /**
     * Test both connection methods
     */
    public static void main(String[] args) {
        System.out.println("=== Testing basic connection ===");
        testConnection(false);

        System.out.println("\n=== Testing retry connection ===");
        testConnection(true);
    }

    private static void testConnection(boolean useRetry) {
        try {
            Connection conn = useRetry ? getConnectionWithRetry() : getConnection();
            conn.close();
            System.out.println("✅ Test passed - Connection working");
        } catch (SQLException e) {
            System.err.println("❌ Test failed: " + e.getMessage());
        }
    }
}
public static Connection getConnection() throws SQLException, InterruptedException {
    int retries = 3;
    while (retries > 0) {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            retries--;
            if (retries == 0) throw e;
            Thread.sleep(1000); // Wait before retry
        }
    }
    throw new SQLException("Failed to connect after retries");
}