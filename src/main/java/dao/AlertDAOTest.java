package dao;

import model.Alert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AlertDAOTest {
    public static void main(String[] args) {
        AlertDAO alertDAO = new AlertDAO();

        String testAlertId = UUID.randomUUID().toString();
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

        // 2. Test READ (Recent Alerts)
        System.out.println("\n=== Testing getRecentAlerts() ===");
        List<Alert> recentAlerts = alertDAO.getRecentAlerts(5);
        if (recentAlerts.isEmpty()) {
            System.out.println("❌ No alerts found");
        } else {
            recentAlerts.forEach(System.out::println);
        }

        // 3. Test READ (User-specific)
        System.out.println("\n=== Testing getAlertsByUser() ===");
        List<Alert> userAlerts = alertDAO.getAlertsByUser(testUserId);
        if (userAlerts.isEmpty()) {
            System.out.println("❌ No alerts found for user");
        } else {
            userAlerts.forEach(System.out::println);
        }

        // 4. Test UPDATE
        System.out.println("\n=== Testing updateAlertSeverity() ===");
        String newSeverity = "High";
        alertDAO.updateAlertSeverity(testAlertId, newSeverity);

        // 5. Test DELETE
        System.out.println("\n=== Testing deleteAlert() ===");
        alertDAO.deleteAlert(testAlertId);

        // 6. Verify Deletion
        System.out.println("\n=== Verifying deletion ===");
        List<Alert> verifyAlerts = alertDAO.getRecentAlerts(10);
        boolean alertStillExists = verifyAlerts.stream()
                .anyMatch(a -> a.getAlertId().equals(testAlertId));
        System.out.println(alertStillExists ? "❌ Deletion failed" : "✅ Deletion verified");
    }
}
