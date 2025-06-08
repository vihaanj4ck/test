package dao;

import model.Alert;
import utils.DBConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {

    // Insert alert into DB
    public boolean insertAlert(Alert alert) {
        String sql = "INSERT INTO alerts (alert_id, type, location, timestamp, severity, user_id, description, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, alert.getAlertId());
            stmt.setString(2, alert.getType());
            stmt.setString(3, alert.getLocation());
            stmt.setTimestamp(4, Timestamp.valueOf(alert.getTimestamp()));
            stmt.setString(5, alert.getSeverity());
            stmt.setString(6, alert.getUserId());
            stmt.setString(7, alert.getDescription());
            stmt.setDouble(8, alert.getLatitude());
            stmt.setDouble(9, alert.getLongitude());

            int rowsInserted = stmt.executeUpdate();
            conn.commit();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all alerts ordered by timestamp desc
    public List<Alert> getAllAlerts() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM alerts ORDER BY timestamp DESC";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alert alert = new Alert(
                        rs.getString("alert_id"),
                        rs.getString("type"),
                        rs.getString("location"),
                        rs.getTimestamp("timestamp").toLocalDateTime(),
                        rs.getString("severity"),
                        rs.getString("user_id"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("description")
                );
                alerts.add(alert);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alerts;
    }

    // DB connection test method
    public boolean testConnection() {
        try (Connection conn = DBConnector.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
