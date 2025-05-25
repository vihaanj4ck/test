package dao;

import utils.DBConnector;
import model.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {
    private final DBConnector dbConnector;

    public AlertDAO() {
        this.dbConnector = new DBConnector();
    }

    public void logAlert(Alert alert) {
        String sql = "INSERT INTO alerts (alert_id, type, location, timestamp, severity, user_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, alert.getAlertId());
            stmt.setString(2, alert.getType());
            stmt.setString(3, alert.getLocation());
            stmt.setTimestamp(4, Timestamp.valueOf(alert.getTimestamp()));
            stmt.setString(5, alert.getSeverity());
            stmt.setString(6, alert.getUserId());

            stmt.executeUpdate();
            System.out.println("✅ Alert logged: " + alert.getAlertId());

        } catch (SQLException e) {
            System.err.println("❌ Error logging alert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Fixed return type to List<Alert>
    public String getRecentAlerts(int limit) {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM alerts ORDER BY timestamp DESC LIMIT ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                alerts.add(new Alert(
                        rs.getString("alert_id"),
                        rs.getString("type"),
                        rs.getString("location"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("severity"),
                        rs.getString("user_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching alerts: " + e.getMessage());
            e.printStackTrace();
        }
        return alerts.toString();
    }

    public void updateAlertSeverity(String alertId, String newSeverity) {
        if (!isValidSeverity(newSeverity)) {
            System.err.println("❌ Invalid severity level: " + newSeverity);
            return;
        }

        String sql = "UPDATE alerts SET severity = ? WHERE alert_id = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newSeverity);
            stmt.setString(2, alertId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Updated severity for alert: " + alertId);
            } else {
                System.out.println("⚠ Alert not found: " + alertId);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error updating alert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isValidSeverity(String severity) {
        return severity != null &&
                (severity.equals("Low") ||
                        severity.equals("Medium") ||
                        severity.equals("High") ||
                        severity.equals("Critical"));
    }

    public void deleteAlert(String alertId) {
        String sql = "DELETE FROM alerts WHERE alert_id = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, alertId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Deleted alert: " + alertId);
            } else {
                System.out.println("⚠ Alert not found: " + alertId);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting alert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Alert> getAlertsByUser(String userId) {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM alerts WHERE user_id = ? ORDER BY timestamp DESC";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                alerts.add(new Alert(
                        rs.getString("alert_id"),
                        rs.getString("type"),
                        rs.getString("location"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("severity"),
                        rs.getString("user_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching user alerts: " + e.getMessage());
        }
        return alerts;
    }
}
