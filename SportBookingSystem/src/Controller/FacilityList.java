package Controller;

import Common.exception;
import Model.Booking;
import Model.Facility;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FacilityList extends ArrayList<Facility> implements Serializable {
    ArrayList<Booking> bookingList = new ArrayList<>();
    SimpleDateFormat dmy = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    String VALID_NAME = "\\D{2,18}";

    public void readFile(String fileName) throws Exception {
        int countSuccess = 0;
        int countFail = 0;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                throw new Exception(exception.ERROR_FILE_NOT_EXIST);
            }
            Scanner sc = new Scanner(file);

            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            while (sc.hasNextLine()) {
                String[] lists = sc.nextLine().split(",");
                if (lists.length != 7) {
                    countFail++;
                    continue;
                }

                String id = lists[0].trim();
                String facilityName = lists[1].trim();
                boolean duplicate = false;
                for (Facility facility : this) {
                    if (facility.getFacilityName().equals(facilityName)) {
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate) {
                    countFail++;
                    continue;
                }
                String facilityType = lists[2].trim();
                String location = lists[3].trim();
                int capacity = 0;
                try {
                    capacity = Integer.parseInt(lists[4].trim());
                    if (capacity < 0) continue;
                } catch (NumberFormatException e) {
                    countFail++;
                    continue;
                }

                Date availabilityStart = null;
                Date availabilityEnd = null;
                try {
                    availabilityStart = dmy.parse(lists[5].trim());
                    availabilityEnd = dmy.parse(lists[6].trim());
                } catch (ParseException e) {
                    countFail++;
                    continue;
                }
                Facility f = new Facility(id, facilityName, facilityType, location, capacity, availabilityStart, availabilityEnd);
                this.add(f);
                countSuccess++;
            }
            System.out.println(countSuccess + " rooms successfully loaded.");
            System.out.println(countFail + " entries failed.");
            System.out.println();
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot read file " + fileName);
        }
    }

    public void saveFile(String fileName) {
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file);
            for (Facility f : this) {
                fw.write(f.toString());
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("cannot write file " + fileName);
        }
    }

    public void updateFacility() throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("ID or Model.Facility Name for update:");
            String idOrName = sc.nextLine();
            try {
                Facility found = getFacilityByIdOrName(idOrName);
                if (found == null) throw new Exception(exception.ERROR_NOT_FOUND);
                System.out.println("Model.Facility information updated for Name: " + "'" + found.getFacilityName() + "'");
                System.out.println(found.toString());
                System.out.println("New Location: ");
                String location = sc.nextLine();
                found.setLocation(location);
                System.out.println("New Capacity: ");
                int capacity = Integer.parseInt(sc.nextLine());
                found.setCapacity(capacity);
                Date availabilityStart = null;
                while (true) {
                    System.out.println("New Availability Start: ");
                    String availabilityStartStr = sc.nextLine();
                    try {
                        availabilityStart = dmy.parse(availabilityStartStr);
                        found.setAvailabilityStart(availabilityStart);
                        break;
                    } catch (ParseException e) {
                        System.out.println(exception.ERROR_DATE);
                    }
                }
                Date availabilityEnd = null;
                while (true) {
                    System.out.println("New Availability End: ");
                    String availabilityEndStr = sc.nextLine();
                    try {
                        availabilityEnd = dmy.parse(availabilityEndStr);
                        if (availabilityEnd.before(availabilityStart)) {
                            System.out.println("End date must be after start date!");
                            continue;
                        }
                        found.setAvailabilityEnd(availabilityEnd);
                        break;
                    } catch (ParseException e) {
                        System.out.println(exception.ERROR_DATE);
                    }
                }
                System.out.println("Model.Facility updated successfully.");
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Facility getFacilityByIdOrName(String idOrName) {
        for (Facility f : this) {
            if (f.getFacilityName().equalsIgnoreCase(idOrName) || f.getId().equalsIgnoreCase(idOrName)) {
                return f;
            }
        }
        return null;
    }

    public void viewFacility() {
        if (this.isEmpty()) {
            System.out.println(exception.ERROR_FACILITY_LIST_EMPTY);
            return;
        }

        System.out.printf("%-20s | %-10s | %-25s | %-8s |%s\n", "Model.Facility Name", "Type", "Location", "Capacity", "Availability");
        System.out.println("---------------------------------------------------------------");

        for (Facility f : this) {
            String endTime = timeFormat.format(f.getAvailabilityEnd());
            System.out.printf("%-20s | %-10s | %-25s | %-8d | %s-%s\n", f.getFacilityName(), f.getFacilityType(), f.getLocation(), f.getCapacity(), dmy.format(f.getAvailabilityStart()), endTime);
        }
    }

    public void bookFacilityService() throws Exception {
        Scanner sc = new Scanner(System.in);
        Booking booking = new Booking();
        while (true) {
            System.out.println("Player name: ");
            String playerName = sc.nextLine();
            if (!playerName.matches(VALID_NAME)){
                System.out.println("player name is invalid!");
                continue;
            }
            else{
                booking.setPlayerName(playerName);
                break;
            }
        }

        Facility f = null;
        while (true) {
            System.out.println("Enter Model.Facility Name: ");
            String facilityName = sc.nextLine();
            boolean check = checkFacilityName(facilityName);
            f = getIdByName(facilityName);
            if (!check) {
                System.out.println("Model.Facility name correspond to a facility or service defined within the system.");
                getFacilityName();
            } else {
                booking.setFacilityName(facilityName);
                break;
            }
        }

        while (true) {
            System.out.println("Enter Date & Time: ");
            String dateTimeStr = sc.nextLine();
            try {
                Date dateTime = dmy.parse(dateTimeStr);
                if(dateTime.after((f.getAvailabilityStart()))){
                    System.out.println("Date & Time must before Start Date");
                    continue;
                }
                booking.setReserved(dateTime);
                break;
            } catch (ParseException e) {
                System.out.println(exception.ERROR_DATE);
            }
        }

        while (true) {
            System.out.println("Enter Duration: ");
            String durationStr = sc.nextLine();
            try {
                int duration = Integer.parseInt(durationStr);
                if (duration < 1 || duration > 5) throw new Exception(exception.ERROR_DURATION);
                booking.setDuration(duration);
                break;
            } catch (Exception e) {
                System.out.println(exception.ERROR_DURATION);
            }
        }
        String bookingCode = generateBookingCode();
        booking.setBookingCode(bookingCode);
        bookingList.add(booking);
        System.out.println("Model.Booking added successfully!");
    }

    public Facility getIdByName(String facilityName) {
        for (Facility f : this) {
            if(f.getFacilityName().equalsIgnoreCase(facilityName)) return f;
        }
        return null;
    }

    public boolean checkFacilityName(String facilityName) {
        for (Facility f : this) {
            if (f.getFacilityName().equalsIgnoreCase(facilityName)) return true;
        }
        return false;
    }

    public void getFacilityName() {
        for (Facility f : this) {
            System.out.println(f.getFacilityName());
        }
    }

    public String generateBookingCode() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            sb.append("BK");
            sb.append(r.nextInt(100));
        }
        return sb.toString();
    }

    public void viewTodayBooking() throws Exception {
        Scanner sc = new Scanner(System.in);
        if (bookingList.isEmpty()) throw new Exception(exception.ERROR_BOOKING_LIST_EMPTY);

        Date date = new Date();
        while (true) {
            System.out.println("Enter Date: ");
            String dateStr = sc.nextLine();
            if (dateStr.isEmpty()) break;
            try {
                date = ymd.parse(dateStr);
                break;
            } catch (ParseException e) {
                System.out.println(exception.ERROR_DATE_FORMAT);
            }
        }

        boolean found = checkBookingAvailable(date);
        if (!found) {
            System.out.println("There are currently no courts or services booked!.");
            return;
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Bookings on " + ymd.format(date));
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-6s| %-20s| %-20s| %-8s%n", "Time", "Model.Facility", "User", "Duration");
        System.out.println("------------------------------------------------------------");
        for (Booking b : bookingList) {
            String bookingDate = ymd.format(b.getReserved());
            String targetDate = ymd.format(date);

            if (bookingDate.equals(targetDate)) {
                System.out.printf("%-6s| %-20s| %-20s| %-8d\n",
                        dmy.format(b.getReserved()), b.getFacilityName(), b.getPlayerName(), b.getDuration());
            }
        }
        System.out.println("------------------------------------------------------------");
    }

    public boolean checkBookingAvailable(Date targetDate) {
        for (Booking b : bookingList) {
            if (ymd.format(b.getReserved()).equals(ymd.format(targetDate))) {
                return true;
            }
        }
        return false;
    }

    public void cancelBooking() throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Enter Model.Booking ID or Player name + Date + Model.Facility");
            String input = sc.nextLine();
            try {
                Booking booking = checkBooking(input);
                if (booking == null) {
                    System.out.println("Model.Booking for ID " + "'" + input + "' could not be found");
                    continue;
                }

                String date = ymd.format(booking.getReserved());
                String time = timeFormat.format(booking.getReserved());
                String facilityId = getIdByFacilityName(booking.getFacilityName());
                boolean check = checkDateBooking(booking);
                if (!check) {
                    System.out.println("This booking (ID: " + booking.getBookingCode() + ") cannot be cancelled!");
                    continue;
                }

                System.out.println("Model.Booking information: ");
                System.out.println("+ Model.Booking ID     : " + booking.getBookingCode());
                System.out.println("+ Player Name    : " + booking.getPlayerName());
                System.out.println("+ Model.Facility Name  : " + booking.getFacilityName() + " " + "[" + " Id: " + facilityId + "]");
                System.out.println("+ Date           : " + date);
                System.out.println("+ Time           : " + time);
                System.out.println("------------------------------------------------------------");
                System.out.print("Do you really want to cancel this court booking? [Y/N]:");
                String choice = sc.nextLine().toLowerCase();
                if (choice.equalsIgnoreCase("y")) {
                    System.out.println("The booking ID " + booking.getBookingCode() + " has been successfully cancelled!");
                    bookingList.remove(booking);
                    break;
                }
                if (choice.equalsIgnoreCase("n")) break;
            } catch (Exception e) {
                System.out.println(exception.ERROR_BOOKING);
            }
        }
    }

    public String getIdByFacilityName(String facilityName) {
        for (Facility f : this) {
            if (f.getFacilityName().equalsIgnoreCase(facilityName)) return f.getId();
        }
        return null;
    }

    public boolean checkDateBooking(Booking booking) throws ParseException {
        for (Facility f : this) {
            if (f.getFacilityName().equalsIgnoreCase(booking.getFacilityName())) {
                if (f.getAvailabilityStart().after(booking.getReserved())) return true;
            }
        }
        return false;
    }

    public Booking checkBooking(String input) {
        for (Booking b : bookingList) {
            if (b.getBookingCode().equalsIgnoreCase(input)) return b;
            String date = ymd.format(b.getReserved());
            String time = timeFormat.format(b.getReserved());
            String combined = (b.getPlayerName() + " " + date + " " + b.getFacilityName().toLowerCase());
            if (combined.equalsIgnoreCase(input.toLowerCase())) return b;
        }
        return null;
    }

    public void monthlyRevenueReport() throws Exception {
        Scanner sc = new Scanner(System.in);
        Map<String, Double> report = new LinkedHashMap<>();
        Map<String, Double> rateMap = new HashMap<>();
        rateMap.put("Badminton", 5000.0);
        rateMap.put("Football", 10000.0);
        rateMap.put("Table Tennis", 3000.0);
        rateMap.put("Swimming Pool", 7000.0);
        rateMap.put("Tennis", 8000.0);

        Date month;
        while (true) {
            System.out.println("Target Month: ");
            String targetMonth = sc.nextLine();
            try {
                month = monthFormat.parse(targetMonth);
                if (month.after(new Date())) throw new Exception(exception.ERROR_FUTURE);
                break;
            } catch (ParseException e) {
                System.out.println(exception.ERROR_MONTH_FORMAT);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (Booking b : bookingList) {
            String format = monthFormat.format(b.getReserved());
            Date convert = monthFormat.parse(format);
            if (convert.equals(month)) {
                for (Facility f : this) {
                    if (f.getFacilityName().equalsIgnoreCase(b.getFacilityName())) {
                        String type = f.getFacilityType();
                        double rate = rateMap.getOrDefault(type, 0.0);
                        double amount = rate * b.getDuration();
                        double count = report.getOrDefault(type, 0.0);
                        report.put(type, count + amount);
                    }
                }
            }
        }

        if (report.isEmpty()) {
            System.out.println("No data available in the Monthly Revenue Report");
            return;
        }

        System.out.println("\nMonthly Revenue Report - '" + monthFormat.format(month) + "'");
        System.out.println("-----------------------------------------");
        System.out.printf("%-4s | %-15s | %-10s\n", "No.", "Model.Facility", "Amount");
        System.out.println("-----------------------------------------");

        double total = 0;
        int count = 1;
        for (Map.Entry<String, Double> entry : report.entrySet()) {
            System.out.printf("%-4d | %-15s | %.0f\n", count++, entry.getKey(), entry.getValue());
            total += entry.getValue();
        }

        System.out.println("-----------------------------------------");
        System.out.printf("Total: %.0f\n", total);
    }

    public void serviceUsageStatistics() {
        Scanner sc = new Scanner(System.in);

        Map<String, Integer> statistic = new HashMap<>();

        for (Booking b : bookingList) {
            for (Facility f : this) {
                if (b.getFacilityName().equalsIgnoreCase(f.getFacilityName())) {
                    String type = f.getFacilityType();
                    int count = statistic.getOrDefault(type, 0);
                    statistic.put(type, count + 1);
                }
            }
        }
        if (statistic.isEmpty()) {
            System.out.println("No data available for Service Usage Statistics!");
            return;
        }

        System.out.println("\nService Usage Statistics Report");
        System.out.println("-----------------------------------------");
        System.out.printf("%-4s | %-15s | %-10s\n", "No.", "Model.Facility Type", "Usage Count");
        System.out.println("-----------------------------------------");

        int index = 1;
        for (Map.Entry<String, Integer> entry : statistic.entrySet()) {
            System.out.printf("%-4d | %-15s | %-10d\n", index++, entry.getKey(), entry.getValue());
        }
        System.out.println("-----------------------------------------");
    }

    public void saveAllData(String fileName) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(this);
            oos.writeObject(bookingList);
            oos.close();
            System.out.println("All data has been successfully saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Cannot save file " + fileName);
        }
    }

    public void loadAllData(String fileName) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            FacilityList loadedFacilities = (FacilityList) ois.readObject();
            ArrayList<Booking> loadedBookings = (ArrayList<Booking>) ois.readObject();
            ois.close();

            this.clear();
            this.addAll(loadedFacilities);
            this.bookingList = loadedBookings;

            System.out.println("Data successfully loaded from " + fileName);
        } catch (Exception e) {
            System.out.println("Cannot load file: " + fileName);
        }
    }
}