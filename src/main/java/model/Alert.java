package model;

import java.time.LocalDateTime;

public class Alert {
    private final String alertId;
    private final String type;
    private final String location;
    private final LocalDateTime timestamp;
    private String severity;

    public Alert(String alertId, String type, String location,
                 LocalDateTime timestamp, String severity, String testUserId) {
        this.alertId = alertId;
        this.type = type;
        this.location = location;
        this.timestamp = timestamp;
        this.severity = severity;
    }

    // Getters and setters
    public String getAlertId() { return alertId; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getModality() {
        return severity; // Fixed: return actual value
    }

    public String getUserId() {
        return "";
    }
}
