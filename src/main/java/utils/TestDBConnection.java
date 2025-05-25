package utils;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection conn = DBConnector.getConnection()) {
            System.out.println("✅ Connected to MySQL database successfully!");
        } catch (Exception e) {
            System.err.println("❌ Failed to connect: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
