package learn.DontWreckMyHouse.data;


import learn.DontWreckMyHouse.models.Host;
import learn.DontWreckMyHouse.models.Reservation;

import java.util.List;

public interface ReservationRepository {

//    1. View Reservations for Host
//2. Make a Reservation
//3. Edit a Reservation
//4. Cancel a Reservation

    List<Reservation> findAllReservationsForHost(String hostEmail) throws DataException; //looks through Hosts file and searches for host via Email, checks for match..(if success) then gets ID, uses ID to check for hostID.csv file, then prints all contents.

    List<Reservation> findReservation(String hostEmail, String guestEmail) throws DataException; //findsALlRes for Host(above) (if success) then checks the guests.csv file for the guestEmail (if founnd), getGuestID then check if guestID matches any guest ID in findAllRes for Host (if success) then print All. .

    Reservation add(Reservation reservation, String hostEmail, String guestEmail) throws DataException; //done, tested. works.

    boolean update(Reservation reservation, String hostEmail, String guestEmail) throws DataException;

    boolean cancelReservation(Reservation reservation, String hostEmail, String guestEmail) throws DataException;
}
