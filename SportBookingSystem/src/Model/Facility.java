package Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Facility implements Serializable {
    private String id;
    private String facilityName;
    private String facilityType;
    private String location;
    private int capacity;
    private Date availabilityStart;
    private Date availabilityEnd;

    SimpleDateFormat dmy = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public Facility() {
        this.id = null;
        this.facilityName = null;
        this.facilityType = null;
        this.location = null;
        this.capacity = 0;
        this.availabilityStart = null;
        this.availabilityEnd = null;
    }

    public Facility(String id, String facilityName, String facilityType, String location, int capacity, Date availabilityStart, Date availabilityEnd) {
        this.id = id;
        this.facilityName = facilityName;
        this.facilityType = facilityType;
        this.location = location;
        this.capacity = capacity;
        this.availabilityStart = availabilityStart;
        this.availabilityEnd = availabilityEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facility) {
        this.facilityType = facility;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Date getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(Date availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public Date getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(Date availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    @Override
    public String toString() {
        return id + ", " +  facilityName + ", " + facilityType + ", " +  location + ", " + capacity + ", " + dmy.format(availabilityStart) + ", " + dmy.format(availabilityEnd);
    }
}
