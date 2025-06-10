package model;

public class DisasterZone {
    private final String zoneId;
    private final String location;
    private final String type;
    private String coordinates;

    public DisasterZone(String zoneId, String location, String type, String coordinates) {
        this.zoneId = zoneId;
        this.location = location;
        this.type = type;
        this.coordinates = coordinates;
    }

    // Getters
    public String getZoneId() { return zoneId; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public String getCoordinates() { return coordinates; }

    // Setter for mutable field
    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}