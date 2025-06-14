package service;

import model.Alert;
import dao.AlertDAO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AlertService {
    private final AlertDAO alertDAO;

    public AlertService(AlertDAO alertDAO) {
        this.alertDAO = alertDAO;
    }

    /**
     * Creates and validates a new alert before saving to the database.
     */
    public boolean createAlert(String type, String location, String severity,
                               String userId, String description,
                               Double latitude, Double longitude) {
        try {
            // Validate required fields
            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("Type is required");
            }
            if (location == null || location.isEmpty()) {
                throw new IllegalArgumentException("Location is required");
            }

            Alert alert = new Alert(
                    null,                   // Let DB auto-generate ID
                    type,
                    location,
                    LocalDateTime.now(),    // Current timestamp
                    severity,
                    userId,
                    latitude,               // latitude (Double)
                    longitude,              // longitude (Double)
                    description             // description (String)
            );

            return alertDAO.insertAlert(alert);
        } catch (Exception e) {
            System.err.println("Error creating alert: " + e.getMessage());
            return false;
        }
    }
    /**
     * Fetches all alerts from the database.
     */
    public List<Alert> getAllAlerts() {
        return alertDAO.getAllAlerts();
    }

    /**
     * Fetches alerts by severity level.
     */
    public List<Alert> getAlertsBySeverity(String severity) {
        return alertDAO.getAllAlerts().stream()
                .filter(alert -> alert.getSeverity().equalsIgnoreCase(severity))
                .collect(Collectors.toList());
    }

    public boolean createAlert(Alert alert) {
        return false;
    }
}