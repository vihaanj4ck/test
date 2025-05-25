package utils;

import java.sql.*;

public class DBConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster_db";
    private static final String USER = "root";
    private static final String PASS = "guviproject";

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);  // Enables manual transaction control
            return conn;
        } catch (SQLException e) {
            throw new SQLException("DB Connection Failed: " + e.getMessage(), e);
        }
    }

    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Failed to close ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Failed to close Statement: " + e.getMessage());
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to close Connection: " + e.getMessage());
        }
    }
}
