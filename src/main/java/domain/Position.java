package domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Class to keep info about a certain position a cartTacker's car was at.
 */
public class Position implements Serializable {

    private Long id;
    private Date date;
    private double latitude;
    private double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = new Date();
    }

    //<editor-fold desc="Getters/Setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    //</editor-fold>

    public Position copy() {
        return new Position(latitude, longitude);
    }
}
