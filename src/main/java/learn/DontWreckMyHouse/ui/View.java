package learn.DontWreckMyHouse.ui;

import learn.DontWreckMyHouse.models.Guest;
import learn.DontWreckMyHouse.models.Host;
import learn.DontWreckMyHouse.models.Reservation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class View {
    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }


    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            if (!option.isHidden()) {
                io.printf("%s. %s%n", option.getValue(), option.getMessage());
            }
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

//************ DISPLAY ONLY ****************
    //*********** display header ***********
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }
    //********* display exception **********
    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }
    //********* display status *************
    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

//************* display for CRUD Methods ******************

    public void displayReservations(List<Reservation> reservations) {

        if(reservations == null){
            io.println("No reservation found.");
            return;
        }

//        assert reservations != null;
        if(reservations.size() == 0 ){
            io.println("No current Reservation found.");
            return;
        }

        io.println("=".repeat(20));
        io.println("Reservations Found: ");

        for(Reservation reservation: reservations){
            io.printf("ID: %s, Start_date: %s, End_date: %s, Guest_Id: %s, Total: %s%n", reservation.getId(), reservation.getStartDate(), reservation.getEndDate(), reservation.getGuestId(), reservation.getTotal());
            //id,start_date,end_date,guest_id,total
        }
        io.println("=".repeat(20));
    }



    //************************** CRUD METHODS ***************************
    //********************************************************************

    //************************ VIEW RESERVATIONS *************************
    public String seeReservations() {
        return io.readRequiredString("Enter Host Email: "); //can add do, while string is not of a certain format
    }

    //************************ MAKE A RESERVATION **************************
    public Reservation makeReservation() {
        Reservation reservation = new Reservation(); //remvove arguments above
        reservation.setStartDate(io.readLocalDate("Enter Start Date [MM/dd/yyyy]: "));
        reservation.setEndDate(io.readLocalDate("Enter End Date [MM/dd/yyyy]:  "));
        return reservation;
                //id,start_date,end_date,guest_id,total
    }

    //**************** support methods for Make a reservation **********************
    public String returnHostEmail(){
        return io.readRequiredString("Enter Host Email Here: ");
    }

    public String returnGuestEmail(){
        return io.readRequiredString("Enter Guest Email Here: ");
    }

    int res_ID = 0;
    public int returnReservationID() {
        res_ID = io.readInt("Enter Reservation ID: ");
        return res_ID;
    }

    //************************ UPDATE A RESERVATION **************************
    public Reservation updateReservation() {
        return makeReservation();
    }

    //************************ CANCEL A RESERVATION **************************

    public void displayReservation(Reservation reservation) {

        if(reservation == null){
            io.println("No reservation found.");
            return;
        } else {
            io.println("=".repeat(20));
            io.println("Reservation: ");
            io.printf("ID: %s, Start_date: %s, End_date: %s, Guest_Id: %s, Total: %s%n", reservation.getId(), reservation.getStartDate(), reservation.getEndDate(), reservation.getGuestId(), reservation.getTotal());
//            io.println("=".repeat(20));
            //id,start_date,end_date,guest_id,total
//          return io.readBoolean("Are you sure you want to cancel this reservation? : [y / n]: " );

        }

    }

    public boolean deleteOrNot(){
        return io.readBoolean("Are you sure you want to cancel reservation "  + res_ID + "?  : [y / n]: "  );

    }

    public boolean proceedOrNot(Reservation reservation, BigDecimal total) {
        io.println("=".repeat(20));
        io.println("Your reservation details are as follows: ");
        io.println("*".repeat(3));
        io.printf("Start Date: %s %nEnd Date: %s %nTotal: %s", reservation.getStartDate(),reservation.getEndDate(),total.setScale(2, RoundingMode.CEILING));
        io.println("");
        io.println("*".repeat(3));
        return io.readBoolean("Do you want to proceed with this reservation? [y / n]: ");

    }
}
