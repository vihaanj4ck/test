package utils;

import java.sql.*;

public class DBConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster_db";
    private static final String USER = "root";
    private static final String PASS = "guviproject";

    @org.jetbrains.annotations.NotNull
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false); // Better transaction control
            return conn;
        } catch (SQLException e) {
            throw new SQLException("DB Connection Failed: " + e.getMessage());
        }
    }

    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing DB resources: " + e.getMessage());
        }
    }
}
