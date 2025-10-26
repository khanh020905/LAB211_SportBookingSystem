package Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking implements Serializable {
    private String bookingCode;
    private String playerName;
    private String facilityName;
    private Date reserved;
    private int duration;

    SimpleDateFormat dmy = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public Booking() {
        this.bookingCode = "";
        this.playerName = "";
        this.facilityName = "";
        this.reserved = new Date();
    }

    public Booking(String playerName, String facilityName, Date reserved, int duration) {
        this.bookingCode = "";
        this.playerName = playerName;
        this.facilityName = facilityName;
        this.reserved = reserved;
        this.duration = duration;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Date getReserved() {
        return reserved;
    }

    public void setReserved(Date reserved) {
        this.reserved = reserved;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return bookingCode + ", " +  playerName + ", " + facilityName + ", " + dmy.format(reserved) + ", " + duration;
    }
}
