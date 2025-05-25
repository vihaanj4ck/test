package dao;

import model.DisasterZone;

public class ZoneDAOTest {
    public static void main(String[] args) {
        ZoneDAO zoneDAO = new ZoneDAO();

        // CREATE Test
        DisasterZone zone = new DisasterZone(
                "ZONE_TEST_001",
                "Mumbai",
                "Flood",
                "19.0760,72.8777,5km"
        );
        zoneDAO.addZone(zone);
        System.out.println("✅ Zone added");

        // READ Test
        System.out.println("\nActive Zones:");
        zoneDAO.getActiveZones().forEach(z ->
                System.out.println(z.getZoneId() + " - " + z.getType())
        );

        // UPDATE Test
        zoneDAO.updateZoneCoordinates("ZONE_TEST_001", "19.0760,72.8777,10km");
        System.out.println("\n✅ Zone updated");

        // DELETE Test
        zoneDAO.deleteZone("ZONE_TEST_001");
        System.out.println("✅ Zone deleted");
    }
}