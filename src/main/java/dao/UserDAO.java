package main.java.dao;

import model.User;
import utils.DBConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // CREATE - Insert a new user
    public void registerUser(User user) {
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "INSERT INTO users (user_id, name, contact, location) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getContact());
            stmt.setString(4, user.getLocation());

            stmt.executeUpdate();
            System.out.println("✅ User registered: " + user.getName());

        } catch (SQLException e) {
            System.err.println("❌ Error registering user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // READ - Get all users in a location
    public List<User> getUsersByLocation(String location) {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "SELECT * FROM users WHERE location = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, location);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching users: " + e.getMessage());
        }
        return users;
    }

    // UPDATE - Modify user's contact
    public void updateContact(String userId, String newContact) {
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "UPDATE users SET contact = ? WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newContact);
            stmt.setString(2, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Updated contact for user: " + userId);
            } else {
                System.out.println("⚠ User not found: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error updating contact: " + e.getMessage());
        }
    }

    // DELETE - Remove a user
    public void deleteUser(String userId) {
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ User deleted: " + userId);
            } else {
                System.out.println("⚠ User not found: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting user: " + e.getMessage());
        }
    }
}