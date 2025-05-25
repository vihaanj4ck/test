package test.java.dao;

import dao.AlertDAO;
import model.Alert;
import java.time.LocalDateTime;
import java.util.List;

public class AlertDAOTest {
    public static void main(String[] args) {
        AlertDAO alertDAO = new AlertDAO();

        // Create a unique test alert ID to avoid conflicts
        String testAlertId = "TEST_" + System.currentTimeMillis();
        String testUserId = "TEST_USER";

        // 1. Test CREATE
        System.out.println("=== Testing logAlert() ===");
        Alert testAlert = new Alert(
                testAlertId,
                "Test Alert",
                "Test Location",
                LocalDateTime.now(),
                "Medium",
                testUserId
        );
        alertDAO.logAlert(testAlert);
        System.out.println("✅ Test alert created: " + testAlertId);

        // 2. Test READ (Recent Alerts)
        System.out.println("\n=== Testing getRecentAlerts() ===");
        String recentAlertsResult = alertDAO.getRecentAlerts(5); // Changed to String
        if (recentAlertsResult == null || recentAlertsResult.isEmpty()) {
            System.out.println("❌ No alerts found");
        } else {
            System.out.println("✅ Recent alerts:\n" + recentAlertsResult);
        }

        // 3. Test READ (User-specific)
        System.out.println("\n=== Testing getAlertsByUser() ===");
        String userAlertsResult = alertDAO.getAlertsByUser(testUserId).toString(); // Changed to String
        if (userAlertsResult == null || userAlertsResult.isEmpty()) {
            System.out.println("❌ No alerts found for user");
        } else {
            System.out.println("✅ User alerts:\n" + userAlertsResult);
        }

        // 4. Test UPDATE
        System.out.println("\n=== Testing updateAlertSeverity() ===");
        String newSeverity = "High";
        alertDAO.updateAlertSeverity(testAlertId, newSeverity);
        System.out.println("✅ Updated severity to: " + newSeverity);

        // 5. Test DELETE
        System.out.println("\n=== Testing deleteAlert() ===");
        alertDAO.deleteAlert(testAlertId);
        System.out.println("✅ Deleted test alert");

        // 6. Verify Deletion
        System.out.println("\n=== Verifying deletion ===");
        String verificationResult = alertDAO.getRecentAlerts(10);
        boolean alertStillExists = verificationResult != null && verificationResult.contains(testAlertId);
        System.out.println(alertStillExists ? "❌ Deletion failed" : "✅ Deletion verified");
    }
}