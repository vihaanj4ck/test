package dao;

import model.Alert;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class AlertDAOTest {
    public static <AlertDAO, Alert> void main(String[] args) {
        AlertDAO alertDAO = new AlertDAO();

        // 1. Test CREATE
        System.out.println("=== Testing logAlert() ===");
        Alert testAlert = new Alert(
                "ALERT_001",
                "Earthquake",
                "San Francisco",
                LocalDateTime.now(),
                "High",
                "U100"  // Use actual user_id that exists in your database
        );
        alertDAO.logAlert(testAlert);

        // 2. Test READ
        System.out.println("\n=== Testing getRecentAlerts() ===");
        List<Alert> recentAlerts = alertDAO.getRecentAlerts(5);
        if (recentAlerts.isEmpty()) {
            System.out.println("No alerts found");
        } else {
            recentAlerts.forEach(alert ->
                    System.out.printf(
                            "%s | %-12s | %-15s | %s\n",
                            alert.getTimestamp(),
                            alert.getType(),
                            alert.getSeverity(),
                            alert.getLocation()
                    )
            );
        }

        // 3. Test READ (User-specific)
        System.out.println("\n=== Testing getAlertsByUser() ===");
        List<Alert> userAlerts = alertDAO.getAlertsByUser("U100");
        userAlerts.forEach(alert ->
                System.out.println(alert.getAlertId() + " - " + alert.getType())
        );

        // 4. Test UPDATE
        System.out.println("\n=== Testing updateAlertSeverity() ===");
        alertDAO.updateAlertSeverity("ALERT_001", "Critical");

        // 5. Test DELETE
        System.out.println("\n=== Testing deleteAlert() ===");
        alertDAO.deleteAlert("ALERT_001");

        // 6. Verify Deletion
        System.out.println("\n=== Verifying deletion ===");
        Alert deletedAlert = alertDAO.getRecentAlerts(1).stream()
                .filter(a -> a.getAlertId().equals("ALERT_001"))
                .findFirst()
                .orElse(null);
        System.out.println(deletedAlert == null ? "✅ Deletion verified" : "❌ Deletion failed");
    }
}
@Test
public void testLogAlert() {
    Alert alert = new Alert(...);
    AlertDAO alertDAO;
    alertDAO.logAlert(alert);
    assertNotNull(alertDAO.getRecentAlerts(1).get(0));
}

void main() {
}