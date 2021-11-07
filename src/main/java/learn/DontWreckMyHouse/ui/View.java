package learn.DontWreckMyHouse.ui;

import learn.DontWreckMyHouse.models.Guest;
import learn.DontWreckMyHouse.models.Host;
import learn.DontWreckMyHouse.models.Reservation;

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

        for(Reservation reservation: reservations){
            io.println("=".repeat(20));
            io.println("Reservations Found: ");
            io.printf("ID: %s, Start_date: %s, End_date: %s, Guest_Id: %s, Total: %s%n", reservation.getId(), reservation.getStartDate(), reservation.getEndDate(), reservation.getGuestId(), reservation.getTotal());
            io.println("=".repeat(20));
            //id,start_date,end_date,guest_id,total
        }
    }



    //************************** CRUD METHODS ***************************
    //********************************************************************

    //************************ View Reservations *************************
    public String seeReservations() {
        return io.readRequiredString("Enter Host Email: "); //can add do, while string is not of a certain format
    }

    //************************ Make a Reservation **************************
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

    public int returnReservationID() {
        return io.readInt("Enter Reservation ID: ");
    }

    //************************ Update a Reservation **************************
    public Reservation updateReservation() {
        return makeReservation();
    }
    //************************ Cancel a Reservation **************************

    public boolean displayReservation(Reservation reservation) {

        if(reservation == null){
            io.println("No reservation found.");
            return false;
        } else {
            io.println("=".repeat(20));
            io.println("Reservation: ");
            io.printf("ID: %s, Start_date: %s, End_date: %s, Guest_Id: %s, Total: %s%n", reservation.getId(), reservation.getStartDate(), reservation.getEndDate(), reservation.getGuestId(), reservation.getTotal());
//            io.println("=".repeat(20));
            //id,start_date,end_date,guest_id,total
          return io.readBoolean("Are you sure you want to cancel this reservation? : [y / n]: " );

        }

    }
//    public Reservation cancelReservation(Reservation toBeCancelled) {
//
//    }
}
