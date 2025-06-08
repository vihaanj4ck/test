package dao;

import model.User;
import utils.DBConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // CREATE - Insert a new user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (user_id, name, contact, location) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getContact());
            stmt.setString(4, user.getLocation());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ User registered: " + user.getName());
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("❌ Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // READ - Get user by ID
    public User getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("location")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching user: " + e.getMessage());
        }
        return null;
    }

    // READ - Get all users in a location
    public List<User> getUsersByLocation(String location) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE location = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
            System.err.println("❌ Error fetching users by location: " + e.getMessage());
        }
        return users;
    }

    // READ - Get all user IDs
    public List<String> getAllUserIds() {
        List<String> userIds = new ArrayList<>();
        String sql = "SELECT user_id FROM users";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userIds.add(rs.getString("user_id"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching user IDs: " + e.getMessage());
        }
        return userIds;
    }

    // UPDATE - Modify user's contact
    public boolean updateContact(String userId, String newContact) {
        String sql = "UPDATE users SET contact = ? WHERE user_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newContact);
            stmt.setString(2, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Updated contact for user: " + userId);
                return true;
            } else {
                System.out.println("⚠ User not found: " + userId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error updating contact: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Remove a user
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ User deleted: " + userId);
                return true;
            } else {
                System.out.println("⚠ User not found: " + userId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting user: " + e.getMessage());
            return false;
        }
    }

    // Test helper method - Delete test user
    public boolean deleteTestUser(String testUserId) {
        return deleteUser(testUserId);
    }

    // Test helper method - Create test user
    public boolean createTestUser(String testUserId, String testUserName, String contact) {
        User testUser = new User(testUserId, testUserName, contact, "Test Location");
        return registerUser(testUser);
    }
}