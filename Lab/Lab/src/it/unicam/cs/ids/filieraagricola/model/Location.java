package it.unicam.cs.ids.filieraagricola.model;

import java.util.Objects;

/**
 * Represents a geographical location within the platform.
 *
 * <p>This class stores coordinate information along with address details
 * and implements the Prototype pattern for defensive copying.</p>
 */
public class Location implements Prototype<Location> {

    private double latitude;
    private double longitude;
    private String address;
    private String municipality;
    private String province;
    private String postalCode;

    /**
     * Default constructor for frameworks.
     */
    public Location() {
        // for frameworks
    }

    /**
     * Full constructor with validation.
     *
     * @param latitude    geographical latitude (-90 to 90)
     * @param longitude   geographical longitude (-180 to 180)
     * @param address     street address (must not be null or empty)
     * @param municipality municipality name (must not be null or empty)
     * @param province    province name (must not be null or empty)
     * @param postalCode  postal code (must not be null or empty)
     * @throws IllegalArgumentException if any validation fails
     */
    public Location(double latitude, double longitude, String address,
                    String municipality, String province, String postalCode) {
        validateCoordinates(latitude, longitude);
        validateAddress(address);
        validateMunicipality(municipality);
        validateProvince(province);
        validatePostalCode(postalCode);

        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address.trim();
        this.municipality = municipality.trim();
        this.province = province.trim();
        this.postalCode = postalCode.trim();
    }

    /**
     * Copy constructor for Prototype pattern.
     *
     * @param other Location to copy (must not be null)
     * @throws NullPointerException if other is null
     */
    public Location(Location other) {
        Objects.requireNonNull(other, "Location to copy cannot be null");
        this.latitude = other.latitude;
        this.longitude = other.longitude;
        this.address = other.address;
        this.municipality = other.municipality;
        this.province = other.province;
        this.postalCode = other.postalCode;
    }

    /**
     * Creates a deep copy of this Location.
     *
     * @return new Location instance identical to this one
     */
    @Override
    public Location clone() {
        return new Location(this);
    }

    // Getters and setters with validation
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        validateLatitude(latitude);
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        validateLongitude(longitude);
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateAddress(address);
        this.address = address.trim();
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        validateMunicipality(municipality);
        this.municipality = municipality.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        validateProvince(province);
        this.province = province.trim();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        validatePostalCode(postalCode);
        this.postalCode = postalCode.trim();
    }

    /**
     * Calculates distance to another location using Haversine formula.
     *
     * @param other other location (must not be null)
     * @return distance in kilometers
     * @throws NullPointerException if other is null
     */
    public double distanceTo(Location other) {
        Objects.requireNonNull(other, "Other location cannot be null");

        double earthRadius = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }

    // Private validation methods
    private static void validateCoordinates(double latitude, double longitude) {
        validateLatitude(latitude);
        validateLongitude(longitude);
    }

    private static void validateLatitude(double latitude) {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
    }

    private static void validateLongitude(double longitude) {
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }

    private static void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
    }

    private static void validateMunicipality(String municipality) {
        if (municipality == null || municipality.trim().isEmpty()) {
            throw new IllegalArgumentException("Municipality cannot be null or empty");
        }
    }

    private static void validateProvince(String province) {
        if (province == null || province.trim().isEmpty()) {
            throw new IllegalArgumentException("Province cannot be null or empty");
        }
    }

    private static void validatePostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Postal code cannot be null or empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        return Double.compare(location.latitude, latitude) == 0 &&
                Double.compare(location.longitude, longitude) == 0 &&
                Objects.equals(address, location.address) &&
                Objects.equals(municipality, location.municipality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, address, municipality);
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", municipality='" + municipality + '\'' +
                '}';
    }
}
