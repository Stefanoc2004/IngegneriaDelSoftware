package it.unicam.cs.ids.filieraagricola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Geolocated point belonging to a {@link SupplyChain} (e.g., farm, market).
 *
 * <p>Stores basic metadata such as coordinates, name, address and phone number.
 * No validation is enforced at this layer beyond field assignment.</p>
 */
@Entity
public class SupplyChainPoint {

    @Id
    private String id;
    private double lat;
    private double lng;
    private String name;
    private String address;
    private String phoneNumber;


    /** Default constructor required by JPA and frameworks. */
    public SupplyChainPoint() {
    }

    /**
     * Convenience constructor initializing coordinates.
     *
     * @param lat latitude
     * @param lng longitude
     */
    public SupplyChainPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    /** Returns the latitude. */
    public double getLat() {
        return lat;
    }

    /** Sets the latitude. */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /** Returns the longitude. */
    public double getLng() {
        return lng;
    }

    /** Sets the longitude. */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /** Returns the identifier. */
    public String getId() {
        return id;
    }

    /** Sets the identifier. */
    public void setId(String id) {
        this.id = id;
    }


    /** Returns the human-readable name. */
    public String getName() {
        return name;
    }

    /** Sets the human-readable name. */
    public void setName(String name) {
        this.name = name;
    }

    /** Returns the address. */
    public String getAddress() {
        return address;
    }

    /** Sets the address. */
    public void setAddress(String address) {
        this.address = address;
    }

    /** Returns the phone number. */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** Sets the phone number. */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Compact string representation including coordinates only.
     */
    @Override
    public String toString() {
        return "SupplyChainPoint{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
