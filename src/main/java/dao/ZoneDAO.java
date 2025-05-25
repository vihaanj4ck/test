package dao;

import model.DisasterZone;
import utils.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZoneDAO {

    // CREATE
    public void addZone(DisasterZone zone) {
        String sql = "INSERT INTO zones (zone_id, location, type, coordinates) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, zone.getZoneId());
            stmt.setString(2, zone.getLocation());
            stmt.setString(3, zone.getType());
            stmt.setString(4, zone.getCoordinates());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error adding zone: " + e.getMessage());
        }
    }

    // READ
    public List<DisasterZone> getActiveZones() {
        List<DisasterZone> zones = new ArrayList<>();
        String sql = "SELECT * FROM zones";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                zones.add(new DisasterZone(
                        rs.getString("zone_id"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getString("coordinates")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching zones: " + e.getMessage());
        }
        return zones;
    }

    // UPDATE
    public void updateZoneCoordinates(String zoneId, String newCoordinates) {
        String sql = "UPDATE zones SET coordinates = ? WHERE zone_id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newCoordinates);
            stmt.setString(2, zoneId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error updating zone: " + e.getMessage());
        }
    }

    // DELETE
    public void deleteZone(String zoneId) {
        String sql = "DELETE FROM zones WHERE zone_id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, zoneId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error deleting zone: " + e.getMessage());
        }
    }
}
