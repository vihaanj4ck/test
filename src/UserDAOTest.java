package dao;

import model.User;
import java.util.List;

public class UserDAOTest {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // 1. Test User Registration
        System.out.println("=== Testing registerUser() ===");
        User newUser = new User("U101", "Rahul", "+919876543210", "Mumbai");
        userDAO.registerUser(newUser);

        // 2. Test Retrieving Users
        System.out.println("\n=== Testing getUsersByLocation() ===");
        List<User> mumbaiUsers = userDAO.getUsersByLocation("Mumbai");
        for (User user : mumbaiUsers) {
            System.out.println(
                    "ID: " + user.getUserId() +
                            " | Name: " + user.getName() +
                            " | Contact: " + user.getContact()
            );
        }

        // 3. Test Updating User
        System.out.println("\n=== Testing updateContact() ===");
        userDAO.updateContact("U101", "+919999999999");

        // 4. Test Deleting User (Optional)
        System.out.println("\n=== Testing deleteUser() ===");
        userDAO.deleteUser("U101");
    }
}