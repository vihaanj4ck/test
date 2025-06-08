package utils;

import java.sql.*;

public class DBConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster_db";
    private static final String USER = "root";
    private static final String PASS = "guviproject";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            throw new SQLException("DB Connection Failed: " + e.getMessage(), e);
        }
    }

    public static Connection getTransactionConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new SQLException("DB Transaction Connection Failed: " + e.getMessage(), e);
        }
    }

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            int rowsAffected = stmt.executeUpdate();
            System.out.println("✅ Rows affected: " + rowsAffected);
            return rowsAffected;
        }
    }

    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt.executeQuery(); // Caller must close conn, stmt, rs manually
    }

    public static void commitTransaction(Connection conn) throws SQLException {
        if (conn != null && !conn.getAutoCommit()) {
            conn.commit();
            System.out.println("✅ Transaction committed successfully");
        }
    }

    public static void rollbackTransaction(Connection conn) {
        try {
            if (conn != null && !conn.getAutoCommit()) {
                conn.rollback();
                System.out.println("🔄 Transaction rolled back");
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to rollback transaction: " + e.getMessage());
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

    public static void closeResources(Connection conn, Statement stmt) {
        closeResources(conn, stmt, null);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }

    public static int deleteById(String table, String idColumn, String id) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", table, idColumn);

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            int rowsDeleted = stmt.executeUpdate();
            System.out.printf("✅ Deleted %d row(s) from %s where %s = '%s'\n", rowsDeleted, table, idColumn, id);
            return rowsDeleted;
        }
    }

    public static int deleteWhere(String table, String whereClause, Object... params) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s", table, whereClause);

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            int rowsDeleted = stmt.executeUpdate();
            System.out.printf("✅ Deleted %d row(s) from %s\n", rowsDeleted, table);
            return rowsDeleted;
        }
    }

    public static int deleteBulk(String table, String idColumn, String... ids) throws SQLException {
        if (ids.length == 0) return 0;

        String placeholders = String.join(",", java.util.Collections.nCopies(ids.length, "?"));
        String sql = String.format("DELETE FROM %s WHERE %s IN (%s)", table, idColumn, placeholders);

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getTransactionConnection();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < ids.length; i++) {
                stmt.setString(i + 1, ids[i]);
            }

            int rowsDeleted = stmt.executeUpdate();
            commitTransaction(conn);
            System.out.printf("✅ Bulk deleted %d row(s) from %s\n", rowsDeleted, table);
            return rowsDeleted;

        } catch (SQLException e) {
            rollbackTransaction(conn);
            throw e;
        } finally {
            closeResources(conn, stmt, null);
        }
    }
}
