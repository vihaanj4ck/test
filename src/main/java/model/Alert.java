package model;

import java.time.LocalDateTime;

public class Alert {
    public static Object AlertType;
    private String alertId;
    private String type;
    private String location;
    private LocalDateTime timestamp;
    private String severity;
    private String userId;
    private String description;
    private Double latitude;
    private Double longitude;

    public Alert(String alertId, String type, String location, LocalDateTime timestamp,
                 String severity, String userId, Double latitude, Double longitude, String description){
        this.alertId = alertId;
        this.type = type;
        this.location = location;
        this.timestamp = timestamp;
        this.severity = severity;
        this.userId = userId;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Alert(String alertId, String type, String location, LocalDateTime now, String severity, String userId, String description, Double latitude, Double longitude) {
    }

    // Getters and Setters
    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Change here: use Double, not double
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    private String modality;

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String alertIdProperty() {
        return alertId;
    }

    public String typeProperty() {
        return type;
    }

    public String severityProperty() {
        return severity;
    }

    public void setTitle(String success) {
    }

    public void setHeaderText(Object o) {
    }

    public void setContentText(String message) {
    }

    public void showAndWait() {
    }
}
