package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;

public class Alert {
    private final StringProperty alertId;
    private final StringProperty type;
    private final StringProperty location;
    private final StringProperty severity;
    private final StringProperty userId;
    private final LocalDateTime timestamp;

    public Alert(String alertId, String type, String location,
                 LocalDateTime timestamp, String severity, String userId) {
        this.alertId = new SimpleStringProperty(alertId);
        this.type = new SimpleStringProperty(type);
        this.location = new SimpleStringProperty(location);
        this.timestamp = timestamp;
        this.severity = new SimpleStringProperty(severity);
        this.userId = new SimpleStringProperty(userId);
    }

    // Getters
    public String getAlertId() { return alertId.get(); }
    public String getType() { return type.get(); }
    public String getLocation() { return location.get(); }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSeverity() { return severity.get(); }
    public String getUserId() { return userId.get(); }

    // Setters
    public void setSeverity(String severity) { this.severity.set(severity); }

    // Property accessors
    public ObservableValue<String> alertIdProperty() { return alertId; }
    public ObservableValue<String> typeProperty() { return type; }
    public ObservableValue<String> severityProperty() { return severity; }

    @Override
    public String toString() {
        return getType() + " - " + getLocation() + " (" + getSeverity() + ")";
    }

    public String getModality() {
        return "";
    }
}