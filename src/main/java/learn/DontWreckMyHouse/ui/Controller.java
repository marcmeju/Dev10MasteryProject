package learn.DontWreckMyHouse.ui;

import learn.DontWreckMyHouse.data.DataException;
import learn.DontWreckMyHouse.domain.GuestService;
import learn.DontWreckMyHouse.domain.HostService;
import learn.DontWreckMyHouse.domain.ReservationService;
import learn.DontWreckMyHouse.domain.Result;
import learn.DontWreckMyHouse.models.Host;
import learn.DontWreckMyHouse.models.Reservation;

import java.math.BigDecimal;
import java.util.List;

public class Controller {

    private final HostService hostService;
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final View view;


    public Controller(HostService hostService, GuestService guestService, ReservationService reservationService, View view) {
        this.hostService = hostService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome: Do Not Wreck My House");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    // EXIT(0, "Exit", false),
    //    MAKE_A_RESERVATION(1, "Make a Reservation", false),
    //    VIEW_RESERVATION(2, "View Reservations for Host", false),
    //    EDIT_A_RESERVATION(3, "Edit a Reservation", false),
    //    CANCEL_A_RESERVATION(4, "Cancel a Reservation", false);

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case MAKE_A_RESERVATION:
                    addReservation();
                    view.enterToContinue();
                    break;
                case VIEW_RESERVATION:
//                    view.displayStatus(false, "NOT IMPLEMENTED");
                    //eyearnes0@sfgate.com
                    viewReservations();
                    view.enterToContinue();
                    break;
                case EDIT_A_RESERVATION:
                    updateReservation();
                    view.enterToContinue();
                    break;
                case CANCEL_A_RESERVATION:
                    cancelReservation();
                    view.enterToContinue();
                    break;

            }
        } while (option != MainMenuOption.EXIT);
    }


    //******************* CRUD METHODS *************************

    private void viewReservations() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATION.getMessage());
        String hostEmail = view.seeReservations();
        List<Reservation> reservations = reservationService.findAllReservationsForHost(hostEmail);
        view.displayReservations(reservations);
    }

    private void addReservation() throws DataException {
        view.displayHeader(MainMenuOption.MAKE_A_RESERVATION.getMessage());
        String hostEmail = view.returnHostEmail();
        String guestEMail = view.returnGuestEmail();
        Reservation reservation = view.makeReservation();
        Host host = hostService.findByEmail(hostEmail);
        BigDecimal total =  reservationService.setTotal(reservation,host);
        boolean proceedOrNot = view.proceedOrNot(reservation, total);

        if(proceedOrNot){
            Result<Reservation> result = reservationService.add(reservation, hostEmail, guestEMail);
            if (!result.isSuccess()) {
                view.displayStatus(false, result.getErrorMessages());
            } else {
                String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
                view.displayStatus(true, successMessage);
            }

        }else {
            String successMessage = String.format("Reservation was cancelled.");
            view.displayStatus(true, successMessage);
        }

    }

    private void updateReservation() throws DataException {
        Result<Reservation> result = new Result<>();
        view.displayHeader(MainMenuOption.EDIT_A_RESERVATION.getMessage());
        String hostEmail = view.returnHostEmail();
        String guestEMail = view.returnGuestEmail();
        List<Reservation> reservations = reservationService.findReservation(hostEmail, guestEMail);
        view.displayReservations(reservations);
        if (reservations == null || reservations.size() == 0) {
            return;
        }
        int reservation_ID = view.returnReservationID();
        Reservation reservation = view.updateReservation();

        Host host = hostService.findByEmail(hostEmail);
        BigDecimal total =  reservationService.setTotal(reservation,host);
        boolean proceedOrNot = view.proceedOrNot(reservation, total);

        if(proceedOrNot) {
            result = reservationService.update(reservation, hostEmail, guestEMail, reservation_ID);

            if (!result.isSuccess()) {
                view.displayStatus(false, result.getErrorMessages());
            } else {
                String successMessage = String.format("Reservation %s updated.", result.getPayload().getId());
                view.displayStatus(true, successMessage);
            }
        } else {
        String successMessage = String.format("Reservation was not updated.");
        view.displayStatus(true, successMessage);
    }
    }

    private void cancelReservation() throws DataException {
        Result<Reservation> result = new Result<>();
        view.displayHeader(MainMenuOption.CANCEL_A_RESERVATION.getMessage());
        String hostEmail = view.returnHostEmail();
        String guestEMail = view.returnGuestEmail();
        List<Reservation> reservations = reservationService.findReservation(hostEmail, guestEMail);
        view.displayReservations(reservations);
        if (reservations == null || reservations.size() == 0) {
            return;
        }
        int reservation_ID = view.returnReservationID();

        List<Reservation> hostRes = reservationService.findReservation(hostEmail, guestEMail);
        Reservation toBeCancelled = null;
        Reservation nada = null;
        boolean cancelOrNot = false;

        for (Reservation r : hostRes) {
            if (reservation_ID != r.getId()) {
                view.displayReservation(null);
                return;
            }
        }

        for (Reservation r : hostRes) {
            if (reservation_ID == r.getId()) {
                toBeCancelled = r;
                cancelOrNot = view.deleteOrNot();
                break;
            } //split yes or no from view
            else if (reservation_ID != r.getId()) {

            } else {cancelOrNot = view.deleteOrNot();
                        return;}
        }
        //remove the boolean aspect of the "if" above and add it below the for loop;
        //do yes or no here!

        if (cancelOrNot) {
            result = reservationService.cancelReservation(toBeCancelled, hostEmail, guestEMail, reservation_ID);
        } else {
            String successMessage = String.format("Reservation %s was not deleted.", reservation_ID);
            view.displayStatus(true, successMessage);
            return;
        }



        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Reservation %s deleted.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }
}
