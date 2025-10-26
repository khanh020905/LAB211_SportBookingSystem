package View;

import Controller.FacilityList;

import java.util.Scanner;

public class MainView {
    Scanner sc = new Scanner(System.in);
    FacilityList fl = new FacilityList();

    public void menu() throws Exception {
        boolean saved = false;

        while(true){
            System.out.println("1. Import Model.Facility From CSV file");
            System.out.println("2. Update Model.Facility Information");
            System.out.println("3. View Facilities & Services");
            System.out.println("4. Book a Model.Facility / Service");
            System.out.println("5. View Today's Bookings");
            System.out.println("6. Cancel a Model.Booking");
            System.out.println("7. Monthly Revenue Report");
            System.out.println("8. Service Usage Statistics");
            System.out.println("9. Save All Data");
            System.out.println("Others- Quit.");
            System.out.println("Your choice: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch(choice){
                case 1: fl.readFile("facility_schedule.csv");
                        fl.saveFile("Active_Room_List.txt");
                        break;
                case 2: fl.updateFacility(); saved = false; break;
                case 3: fl.viewFacility(); saved = false; break;
                case 4: fl.bookFacilityService(); saved = false; break;
                case 5: fl.viewTodayBooking(); saved = false; break;
                case 6: fl.cancelBooking(); saved = false; break;
                case 7: fl.monthlyRevenueReport(); saved = false; break;
                case 8: fl.serviceUsageStatistics(); saved = false; break;
                case 9: fl.saveAllData("BookingInfor.dat");
                saved = true;
                break;
                default:
                    if(!saved){savedData();}
                    System.exit(0);
            }
        }
    }

    public void savedData(){
        System.out.println("Model.Booking data is unsaved!");
        System.out.println("do you want to exit without saving? (Y/N)");
        String choice = sc.nextLine().toLowerCase();
        if(choice.equalsIgnoreCase("y")) System.exit(0);
        else if(choice.equalsIgnoreCase("n")){
            fl.saveAllData("BookingInfor.dat");
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception { new MainView().menu();}
}
