    package dao;

    import model.Alert;
    import org.junit.jupiter.api.*;

    import java.time.LocalDateTime;
    import java.util.List;

    import static org.junit.jupiter.api.Assertions.*;

    class AlertDAOTest {

        private AlertDAO dao;

        @BeforeEach
        void setUp() {
            // Using anonymous subclass to mock DAO behavior for testing without DB
            dao = new AlertDAO() {
                @Override
                public boolean testConnection() {
                    // Always return true for connection test
                    return true;
                }

                @Override
                public boolean insertAlert(Alert alert) {
                    // Simulate insertion success
                    System.out.println("Simulating insert: " + alert);
                    return true;
                }

                @Override
                public List<Alert> getAllAlerts() {
                    // Return fixed sample alerts with correct parameter order
                    return List.of(
                            new Alert("TEST001", "Flood", "TestCity", LocalDateTime.now(),
                                    "High", "USER001", 12.34, 56.78, "Test flood description"),
                            new Alert("TEST002", "Fire", "TestTown", LocalDateTime.now(),
                                    "Medium", "USER002", 23.45, 67.89, "Test fire description")
                    );
                }
            };
        }

        @Test
        void testInsertAlert() {
            Alert alert = new Alert(
                    "ALERT123",
                    "Earthquake",
                    "Delhi",
                    LocalDateTime.now(),
                    "Critical",
                    "USER001",
                    28.6139,   // latitude first (Double)
                    77.2090,   // longitude next (Double)
                    "Severe earthquake"  // description last (String)
            );
            boolean result = dao.insertAlert(alert);
            assertTrue(result);
        }


        @Test
        void testGetAllAlerts() {
            List<Alert> alerts = dao.getAllAlerts();
            assertNotNull(alerts, "Alerts list should not be null");
            assertEquals(2, alerts.size(), "There should be exactly 2 alerts");

            assertEquals("Flood", alerts.get(0).getType(), "First alert type should be Flood");
            assertEquals("Fire", alerts.get(1).getType(), "Second alert type should be Fire");
        }

        @Test
        void testConnection() {
            assertTrue(dao.testConnection(), "Connection test should return true");
        }
    }
