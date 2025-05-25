package model;

import java.time.LocalDateTime;

public class Alert {
    private final String alertId;
    private final String type;
    private final String location;
    private final LocalDateTime timestamp;
    private String severity;
    private final String userId;

    // Constructor with all fields
    public Alert(String alertId, String type, String location,
                 LocalDateTime timestamp, String severity, String userId) {
        if (alertId == null || alertId.isEmpty()) {
            throw new IllegalArgumentException("Alert ID cannot be null or empty");
        }
        this.alertId = alertId;
        this.type = type;
        this.location = location;
        this.timestamp = timestamp;
        this.severity = severity;
        this.userId = userId;
    }

    // Getters
    public String getAlertId() {
        return alertId;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSeverity() {
        return severity;
    }

    public String getUserId() {
        return userId;
    }

    // Setter only for severity (since it's mutable)
    public void setSeverity(String severity) {
        if (!isValidSeverity(severity)) {
            throw new IllegalArgumentException("Invalid severity level: " + severity);
        }
        this.severity = severity;
    }

    // Validation for severity levels
    private boolean isValidSeverity(String severity) {
        return severity != null &&
                (severity.equals("Low") ||
                        severity.equals("Medium") ||
                        severity.equals("High") ||
                        severity.equals("Critical"));
    }

    @Override
    public String toString() {
        return String.format(
                "Alert[ID: %s | Type: %-9s | Location: %-15s | Time: %s | Severity: %-8s | User: %s]",
                alertId,
                type,
                location,
                timestamp.toString().replace("T", " "),
                severity,
                userId
        );
    }

    // Optional: For comparing alerts
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return alertId.equals(alert.alertId);
    }

    @Override
    public int hashCode() {
        return alertId.hashCode();
    }
}