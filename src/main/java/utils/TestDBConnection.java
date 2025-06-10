package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TestDBConnection {
    public static void main(String[] args) {
        System.out.println("🔍 Testing Database Connection and Transactions");
        System.out.println("=================================================\n");

        // Test 1: Basic Connection
        testBasicConnection();

        // Test 2: Transaction Connection
        testTransactionConnection();

        // Test 3: Simple Operations
        testSimpleOperations();

        // Test 4: Transaction Operations
        testTransactionOperations();

        // Test 5: Connection Health
        testConnectionHealth();

        System.out.println("\n🎯 All tests completed!");
    }

    private static void testBasicConnection() {
        System.out.println("📌 TEST 1: Basic Connection (Auto-commit ON)");
        Connection conn = null;
        ResultSet tables = null;

        try {
            // Test basic connection
            conn = DBConnector.getConnection();
            System.out.println("✅ Connected to MySQL database successfully!");

            // Get database metadata
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println("\n📊 Database Metadata:");
            System.out.printf("🔗 URL: %s\n", metaData.getURL());
            System.out.printf("👤 User: %s\n", metaData.getUserName());
            System.out.printf("🚀 Driver: %s %s\n",
                    metaData.getDriverName(),
                    metaData.getDriverVersion());
            System.out.printf("💾 DB: %s %s\n",
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion());

            // Check auto-commit status
            System.out.printf("\n🔧 Auto-commit status: %s ✅\n",
                    conn.getAutoCommit() ? "ON (Expected)" : "OFF (Unexpected!)");

            // List tables
            tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("\n📋 Tables in 'disaster_db':");
            System.out.println("TABLE_NAME\t\tTABLE_TYPE");
            System.out.println("--------------------------------");
            while (tables.next()) {
                System.out.printf("%-20s\t%s\n",
                        tables.getString("TABLE_NAME"),
                        tables.getString("TABLE_TYPE"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Basic Connection Test Failed:");
            printSQLError(e);
        } finally {
            DBConnector.closeResources(conn, null, tables);
        }

        System.out.println("\n" + "=".repeat(50));
    }

    private static void testTransactionConnection() {
        System.out.println("\n📌 TEST 2: Transaction Connection (Auto-commit OFF)");
        Connection conn = null;

        try {
            conn = DBConnector.getTransactionConnection();
            System.out.println("✅ Transaction connection established!");

            System.out.printf("🔧 Auto-commit status: %s ✅\n",
                    conn.getAutoCommit() ? "ON (Error!)" : "OFF (Expected)");

            System.out.printf("🔒 Transaction Isolation Level: %d\n",
                    conn.getTransactionIsolation());

        } catch (SQLException e) {
            System.err.println("❌ Transaction Connection Test Failed:");
            printSQLError(e);
        } finally {
            DBConnector.closeResources(conn, null, null);
        }

        System.out.println("\n" + "=".repeat(50));
    }

    private static void testSimpleOperations() {
        System.out.println("\n📌 TEST 3: Simple Operations (Auto-commit)");

        try {
            // Test if we can create a test table (if it doesn't exist)
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS test_alerts (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    alert_id VARCHAR(50) UNIQUE,
                    alert_type VARCHAR(20),
                    location VARCHAR(100),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

            DBConnector.executeUpdate(createTableSQL);
            System.out.println("✅ Test table ready");

            // Insert a test alert
            String testAlertId = "TEST_ALERT_" + System.currentTimeMillis();
            DBConnector.executeUpdate(
                    "INSERT INTO test_alerts (alert_id, alert_type, location) VALUES (?, ?, ?)",
                    testAlertId, "TEST", "Test Location"
            );
            System.out.println("✅ Test alert inserted with auto-commit");

            // Verify it's there immediately
            verifyTestAlert(testAlertId);

            // Clean up
            DBConnector.executeUpdate("DELETE FROM test_alerts WHERE alert_id = ?", testAlertId);
            System.out.println("✅ Test alert cleaned up");

        } catch (SQLException e) {
            System.err.println("❌ Simple Operations Test Failed:");
            printSQLError(e);
        }

        System.out.println("\n" + "=".repeat(50));
    }

    private static void testTransactionOperations() {
        System.out.println("\n📌 TEST 4: Transaction Operations");
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try {
            conn = DBConnector.getTransactionConnection();

            String testAlertId1 = "TRANS_TEST_1_" + System.currentTimeMillis();
            String testAlertId2 = "TRANS_TEST_2_" + System.currentTimeMillis();

            // Prepare statements for transaction
            stmt1 = conn.prepareStatement(
                    "INSERT INTO test_alerts (alert_id, alert_type, location) VALUES (?, ?, ?)"
            );
            stmt2 = conn.prepareStatement(
                    "INSERT INTO test_alerts (alert_id, alert_type, location) VALUES (?, ?, ?)"
            );

            // Execute first insert
            stmt1.setString(1, testAlertId1);
            stmt1.setString(2, "TRANSACTION_TEST");
            stmt1.setString(3, "Location 1");
            stmt1.executeUpdate();
            System.out.println("✅ First insert executed (not committed yet)");

            // Execute second insert
            stmt2.setString(1, testAlertId2);
            stmt2.setString(2, "TRANSACTION_TEST");
            stmt2.setString(3, "Location 2");
            stmt2.executeUpdate();
            System.out.println("✅ Second insert executed (not committed yet)");

            // At this point, alerts are not visible from other connections
            System.out.println("⏳ Before commit - alerts are not visible from other connections");

            // Commit the transaction
            DBConnector.commitTransaction(conn);
            System.out.println("✅ Transaction committed - alerts are now visible");

            // Verify both alerts are there
            verifyTestAlert(testAlertId1);
            verifyTestAlert(testAlertId2);

            // Clean up
            DBConnector.executeUpdate("DELETE FROM test_alerts WHERE alert_id IN (?, ?)",
                    testAlertId1, testAlertId2);
            System.out.println("✅ Transaction test alerts cleaned up");

        } catch (SQLException e) {
            System.err.println("❌ Transaction Operations Test Failed:");
            DBConnector.rollbackTransaction(conn);
            printSQLError(e);
        } finally {
            DBConnector.closeResources(conn, stmt1, null);
            if (stmt2 != null) {
                try { stmt2.close(); } catch (SQLException e) { /* ignore */ }
            }
        }

        System.out.println("\n" + "=".repeat(50));
    }

    private static void testConnectionHealth() {
        System.out.println("\n📌 TEST 5: Connection Health Check");

        boolean isHealthy = DBConnector.testConnection();
        System.out.println(isHealthy ?
                "✅ Database connection is healthy" :
                "❌ Database connection is unhealthy");

        System.out.println("\n" + "=".repeat(50));
    }

    private static void verifyTestAlert(String alertId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnector.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM test_alerts WHERE alert_id = ?");
            stmt.setString(1, alertId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.printf("✅ Alert verified: %s [%s] at %s\n",
                        rs.getString("alert_id"),
                        rs.getString("alert_type"),
                        rs.getString("location"));
            } else {
                System.out.printf("❌ Alert NOT found: %s\n", alertId);
            }

        } catch (SQLException e) {
            System.err.printf("❌ Failed to verify alert %s: %s\n", alertId, e.getMessage());
        } finally {
            DBConnector.closeResources(conn, stmt, rs);
        }
    }

    private static void printSQLError(SQLException e) {
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Message: " + e.getMessage());

        if (e.getErrorCode() == 0) {
            System.err.println("\n⚠️ Possible Issues:");
            System.err.println("- MySQL server not running?");
            System.err.println("- Wrong credentials?");
            System.err.println("- Database 'disaster_db' doesn't exist?");
            System.err.println("- Network connectivity issues?");
        }
    }
}