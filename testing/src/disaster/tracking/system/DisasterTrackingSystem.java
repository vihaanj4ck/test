// Review 1: Disaster Tracking and Rapid Alert System Implementation
// Creating the new project with JDK & IDE setup
// Define the project structure
package disaster.tracking.system;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

// Design the database schema for the project
/**
 * Database Schema:
 * - disasters (id: String, type: String, location: String, severity: int,
 *              startTime: LocalDateTime, endTime: LocalDateTime, status: String)
 * - alerts (id: String, disasterId: String, message: String,
 *           issuedTime: LocalDateTime, affectedAreas: List<String>)
 */

// Create Files to work on the Project (to store, Retrieve, update)
public class DisasterTrackingSystem {
    private static final String DISASTERS_FILE = "disasters.dat";
    private static final String ALERTS_FILE = "alerts.dat";

    // Implement IO connectivity using IO package
    private List<Disaster> disasters;
    private List<Alert> alerts;

    public DisasterTrackingSystem() {
        this.disasters = new ArrayList<>();
        this.alerts = new ArrayList<>();
        loadData();
    }

    // Create Model classes
    public static class Disaster implements Serializable {
        private String id;
        private String type;
        private String location;
        private int severity;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String status;

        public Disaster(String id, String type, String location, int severity,
                        LocalDateTime startTime, String status) {
            this.id = id;
            this.type = type;
            this.location = location;
            this.severity = severity;
            this.startTime = startTime;
            this.status = status;
        }

        // Getters and setters
        public String getId() { return id; }
        public String getType() { return type; }
        public String getLocation() { return location; }
        public int getSeverity() { return severity; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public String getStatus() { return status; }

        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class Alert implements Serializable {
        private String id;
        private String disasterId;
        private String message;
        private LocalDateTime issuedTime;
        private List<String> affectedAreas;

        public Alert(String id, String disasterId, String message,
                     LocalDateTime issuedTime, List<String> affectedAreas) {
            this.id = id;
            this.disasterId = disasterId;
            this.message = message;
            this.issuedTime = issuedTime;
            this.affectedAreas = affectedAreas;
        }

        // Getters
        public String getId() { return id; }
        public String getDisasterId() { return disasterId; }
        public String getMessage() { return message; }
        public LocalDateTime getIssuedTime() { return issuedTime; }
        public List<String> getAffectedAreas() { return affectedAreas; }
    }

    // Create DAO classes for the IO CRUD operations
    private void loadData() {
        try (ObjectInputStream disInput = new ObjectInputStream(new FileInputStream(DISASTERS_FILE));
             ObjectInputStream alertInput = new ObjectInputStream(new FileInputStream(ALERTS_FILE))) {

            disasters = (List<Disaster>) disInput.readObject();
            alerts = (List<Alert>) alertInput.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing data files found. Starting with empty datasets.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        try (ObjectOutputStream disOutput = new ObjectOutputStream(new FileOutputStream(DISASTERS_FILE));
             ObjectOutputStream alertOutput = new ObjectOutputStream(new FileOutputStream(ALERTS_FILE))) {

            disOutput.writeObject(disasters);
            alertOutput.writeObject(alerts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CRUD operations for Disasters
    public void addDisaster(Disaster disaster) {
        disasters.add(disaster);
        saveData();
    }

    public Disaster getDisaster(String id) {
        return disasters.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void updateDisaster(String id, LocalDateTime endTime, String status) {
        Disaster disaster = getDisaster(id);
        if (disaster != null) {
            disaster.setEndTime(endTime);
            disaster.setStatus(status);
            saveData();
        }
    }

    public void deleteDisaster(String id) {
        disasters.removeIf(d -> d.getId().equals(id));
        saveData();
    }

    // CRUD operations for Alerts
    public void addAlert(Alert alert) {
        alerts.add(alert);
        saveData();
    }

    public Alert getAlert(String id) {
        return alerts.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Aesthetics and Visual Appeal of the UI (Console based)
    // Component Placement and Alignment in the UI
    // Responsiveness and Accessibility of the UI
    public void displayMainMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Disaster Tracking and Rapid Alert System ===");
            System.out.println("1. Report New Disaster");
            System.out.println("2. Issue Alert");
            System.out.println("3. View Active Disasters");
            System.out.println("4. View All Alerts");
            System.out.println("5. Update Disaster Status");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    reportDisaster(scanner);
                    break;
                case 2:
                    issueAlert(scanner);
                    break;
                case 3:
                    viewActiveDisasters();
                    break;
                case 4:
                    viewAllAlerts();
                    break;
                case 5:
                    updateDisasterStatus(scanner);
                    break;
                case 6:
                    System.out.println("Exiting system...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void reportDisaster(Scanner scanner) {
        System.out.println("\n--- Report New Disaster ---");
        System.out.print("Disaster ID: ");
        String id = scanner.nextLine();
        System.out.print("Type (Earthquake/Flood/Fire/Hurricane): ");
        String type = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Severity (1-5): ");
        int severity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Status (Active/Contained/Resolved): ");
        String status = scanner.nextLine();

        Disaster disaster = new Disaster(id, type, location, severity, LocalDateTime.now(), status);
        addDisaster(disaster);
        System.out.println("Disaster reported successfully!");
    }

    private void issueAlert(Scanner scanner) {
        System.out.println("\n--- Issue Alert ---");
        System.out.print("Alert ID: ");
        String id = scanner.nextLine();
        System.out.print("Associated Disaster ID: ");
        String disasterId = scanner.nextLine();
        System.out.print("Alert Message: ");
        String message = scanner.nextLine();

        System.out.print("Number of affected areas: ");
        int areaCount = scanner.nextInt();
        scanner.nextLine();
        List<String> affectedAreas = new ArrayList<>();
        for (int i = 0; i < areaCount; i++) {
            System.out.print("Area " + (i+1) + ": ");
            affectedAreas.add(scanner.nextLine());
        }

        Alert alert = new Alert(id, disasterId, message, LocalDateTime.now(), affectedAreas);
        addAlert(alert);
        System.out.println("Alert issued successfully!");
    }

    private void viewActiveDisasters() {
        System.out.println("\n--- Active Disasters ---");
        disasters.stream()
                .filter(d -> "Active".equals(d.getStatus()))
                .forEach(this::printDisasterDetails);
    }

    private void printDisasterDetails(Disaster disaster) {
        System.out.println("\nID: " + disaster.getId());
        System.out.println("Type: " + disaster.getType());
        System.out.println("Location: " + disaster.getLocation());
        System.out.println("Severity: " + disaster.getSeverity());
        System.out.println("Start Time: " + disaster.getStartTime());
        System.out.println("Status: " + disaster.getStatus());
    }

    private void viewAllAlerts() {
        System.out.println("\n--- All Alerts ---");
        alerts.forEach(this::printAlertDetails);
    }

    private void printAlertDetails(Alert alert) {
        System.out.println("\nAlert ID: " + alert.getId());
        System.out.println("Disaster ID: " + alert.getDisasterId());
        System.out.println("Message: " + alert.getMessage());
        System.out.println("Issued Time: " + alert.getIssuedTime());
        System.out.println("Affected Areas: " + String.join(", ", alert.getAffectedAreas()));
    }

    private void updateDisasterStatus(Scanner scanner) {
        System.out.println("\n--- Update Disaster Status ---");
        System.out.print("Enter Disaster ID: ");
        String id = scanner.nextLine();
        System.out.print("New Status (Active/Contained/Resolved): ");
        String status = scanner.nextLine();

        if (status.equals("Resolved")) {
            System.out.print("End Time (YYYY-MM-DDTHH:MM): ");
            LocalDateTime endTime = LocalDateTime.parse(scanner.nextLine());
            updateDisaster(id, endTime, status);
        } else {
            updateDisaster(id, null, status);
        }
        System.out.println("Disaster status updated successfully!");
    }

    public static void main(String[] args) {
        DisasterTrackingSystem system = new DisasterTrackingSystem();
        system.displayMainMenu();
    }
}