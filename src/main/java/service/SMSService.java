package service;

import model.Alert;
import java.util.List;

public class SMSService {
    // Mock implementation (replace with real SMS API like Twilio, AWS SNS, etc.)
    public boolean sendSMS(String phoneNumber, String message) {
        System.out.println("[SMS] Sending to " + phoneNumber + ": " + message);
        return true; // Assume success
    }

    /**
     * Sends SMS notifications for critical alerts.
     */
    public void notifyCriticalAlerts(List<Alert> alerts, String adminPhoneNumber) {
        alerts.stream()
                .filter(alert -> "Critical".equalsIgnoreCase(alert.getSeverity()))
                .forEach(alert -> {
                    String smsMessage = String.format(
                            "ALERT: %s at %s (Severity: %s)",
                            alert.getType(),
                            alert.getLocation(),
                            alert.getSeverity()
                    );
                    sendSMS(adminPhoneNumber, smsMessage);
                });
    }
}