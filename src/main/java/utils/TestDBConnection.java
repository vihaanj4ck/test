package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        Connection conn = null;
        ResultSet tables = null;

        try {
            // 1. Test basic connection
            conn = DBConnector.getConnection();
            System.out.println("✅ Connected to MySQL database successfully!");

            // 2. Get and display database metadata
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println("\n📌 Database Metadata:");
            System.out.printf("🔗 URL: %s\n", metaData.getURL());
            System.out.printf("👤 User: %s\n", metaData.getUserName());
            System.out.printf("🚀 Driver: %s %s\n",
                    metaData.getDriverName(),
                    metaData.getDriverVersion());
            System.out.printf("💾 DB: %s %s\n",
                    metaData.getDatabaseProductName(),
                    metaData.getDatabaseProductVersion());

            // 3. Test transaction support
            System.out.printf("\n⚙️ Transaction Support: %s\n",
                    metaData.supportsTransactions() ? "YES" : "NO");
            System.out.printf("🔒 Current Isolation Level: %d\n",
                    conn.getTransactionIsolation());

            // 4. List tables with additional info
            tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("\n📋 Tables in 'disaster_db':");
            System.out.println("TABLE_NAME\t\tTABLE_TYPE\t\tREMARKS");
            System.out.println("--------------------------------------------------");
            while (tables.next()) {
                System.out.printf("%-20s\t%-15s\t%s\n",
                        tables.getString("TABLE_NAME"),
                        tables.getString("TABLE_TYPE"),
                        tables.getString("REMARKS"));
            }

            // 5. Verify auto-commit is OFF (as set in DBConnector)
            System.out.printf("\n🔧 Auto-commit status: %s\n",
                    conn.getAutoCommit() ? "ON (Warning!)" : "OFF (Correct)");

        } catch (SQLException e) {
            System.err.println("\n❌ SQL Error:");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());

            // Print more details for connection errors
            if (e.getErrorCode() == 0) { // Common for connection failures
                System.err.println("\n⚠️ Check:");
                System.err.println("- Is MySQL server running?");
                System.err.println("- Are credentials correct?");
                System.err.println("- Is the database name correct?");
            }

        } finally {
            // 6. Properly close resources
            DBConnector.closeResources(conn, null, tables);
            System.out.println("\n🔌 All database resources closed.");
        }
    }
}