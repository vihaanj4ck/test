package utils;

public class InputValidator {

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean isValidContact(String contact) {
        return contact != null && contact.matches("\\d{10}");
    }

    public static boolean isValidUserId(String userId) {
        return userId != null && userId.matches("[a-zA-Z0-9_-]{3,20}");
    }

    public static boolean isValidLocation(String location) {
        return location != null && location.trim().length() >= 2;
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z\\s]{2,50}");
    }

    public static boolean isUserValid(String userId, String name, String contact, String location) {
        return isValidUserId(userId) &&
                isValidName(name) &&
                isValidContact(contact) &&
                isValidLocation(location);
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}
