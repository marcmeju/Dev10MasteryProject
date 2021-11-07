package learn.DontWreckMyHouse.domain;

import learn.DontWreckMyHouse.data.DataException;
import learn.DontWreckMyHouse.data.GuestRepository;
import learn.DontWreckMyHouse.data.HostRepository;
import learn.DontWreckMyHouse.data.ReservationRepository;
import learn.DontWreckMyHouse.models.Guest;
import learn.DontWreckMyHouse.models.Host;
import learn.DontWreckMyHouse.models.Reservation;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;


    public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository, HostRepository hostRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }


    //******************************** METHODS ********************************
    //**************************** find All Reservations **********************
    public List<Reservation> findAllReservationsForHost(String hostEmail) throws DataException {
        //validate inputs
        Result result = new Result();
//        validateEmail(hostEmail, result);
//        if(!result.isSuccess()){
//            return result;
//        }
        return reservationRepository.findAllReservationsForHost(hostEmail);
    }

    //************************ find Reservation *******************************
    public List<Reservation> findReservation(String hostEmail, String guestEmail) throws DataException {

        //validate inputs; might have to do something if null is returned

        return reservationRepository.findReservation(hostEmail, guestEmail);
    }


    //************************* Add a Reservation ****************************
    public Result<Reservation> add(Reservation reservation, String hostEmail, String guestEmail) throws DataException {

        Result<Reservation> result = new Result<>();
        validateEmail(hostEmail, result); //add if is succssc add more more to email validation, e.g. email format
        // I guess this part should run the finding of all res itself.
        //validate other inputs in reserv.
        if(!result.isSuccess()){
            return result;
        }
        Host host = hostRepository.findByEmail(hostEmail);
        if (host == null) {
            result.addErrorMessage("Check host Email. No host with that email address exists.");
            return result;
        }

        validateNulls(reservation);

        if(!result.isSuccess()){
            return result;
        }

        validateFields(reservation, result);

        if(!result.isSuccess()){
            return result;
        }



        reservation.setHost(host);

        //val domain
        //check that the date are not taken res.start date  and end date do not over lapp any taken date already
        validateDomain(reservation, hostEmail, result);

        if(!result.isSuccess()){
            return result;
        }

        //*** validate guest Email
        validateEmail(guestEmail, result); // is success!

        Guest guest = guestRepository.findGuest(guestEmail);
        //if null
        if (guest == null) {
            result.addErrorMessage("Check guest email. No guest with that email address exists.");
            return result;
        } else reservation.setGuest(guest);

        reservation.setGuestId(guest.getGuestID());


        BigDecimal tot = new BigDecimal(0);
        BigDecimal total;
        LocalDate start = reservation.getStartDate();

        for(start = start; start.getDayOfYear() < (reservation.getEndDate().getDayOfYear()) ; start = start.plusDays(1)){
            if(start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY  ){
                tot = tot.add(host.getWeekendRate());
            }else tot = tot.add(host.getStandardRate());

        }
        reservation.setTotal(tot);
//        reservation.getStartDate().compareTo(reservation.getEndDate())
//        host.getStandardRate();


        //validate inputs
        //if is success
        //validate domain;
        result.setPayload(reservationRepository.add(reservation, hostEmail, guestEmail));

        return result;
    }




    public Result<Reservation> update(Reservation reservation, String hostEmail, String guestEmail, int reservation_ID) throws DataException {

        Result<Reservation> result = new Result<>();
        validateEmail(hostEmail, result); //add if is succssc add more more to email validation, e.g. email format
        // I guess this part should run the finding of all res itself.
        //validate other inputs in reserv.
        if(!result.isSuccess()){
            return result;
        }
        validateEmail(guestEmail, result); // is success!
        if(!result.isSuccess()){
            return result;
        }

        Host host = hostRepository.findByEmail(hostEmail);
        List<Reservation> allGuestReservationsWithHost = findReservation(hostEmail, guestEmail);

        if (host == null) {
            result.addErrorMessage("Check host Email. No host with that email address exists.");
            return result;
        }

        validateNulls(reservation);

        if(!result.isSuccess()){
            return result;
        }

        validateFields(reservation, result);

        if(!result.isSuccess()){
            return result;
        }

        reservation.setHost(host);

        //val domain
        //check that the date are not taken res.start date  and end date do not over lapp any taken date already
        validateDomain(reservation, hostEmail, result);

        if(!result.isSuccess()){
            return result;
        }

        //*** validate guest Email
        validateEmail(guestEmail, result); // is success!

        Guest guest = guestRepository.findGuest(guestEmail);
        //if null
        if (guest == null) {
            result.addErrorMessage("Check guest email. No guest with that email address exists.");
            return result;
        } else reservation.setGuest(guest);

        reservation.setGuestId(guest.getGuestID());

        //Here. It goes here!
Reservation toBeUpdated =
        allGuestReservationsWithHost.stream().filter(reservation1 -> reservation1.getId() == reservation_ID)
                .findFirst()
                .orElse(null);
        if(toBeUpdated == null){
            result.addErrorMessage("Invalid reservation ID.");
            return result;
        }else reservation.setId(reservation_ID);

        //loop through the list of guest reservations with host. Get IDs. If reservation ID is not equal to any addErrMEs: Wrong ID inputted. Before setting it, actually


        BigDecimal tot = new BigDecimal(0);
        BigDecimal total;
        LocalDate start = reservation.getStartDate();

        for(start = start; start.getDayOfYear() < (reservation.getEndDate().getDayOfYear()) ; start = start.plusDays(1)){
            if(start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY  ){
                tot = tot.add(host.getWeekendRate());
            }else tot = tot.add(host.getStandardRate());

        }
        reservation.setTotal(tot);
//        reservation.getStartDate().compareTo(reservation.getEndDate())
//        host.getStandardRate();


        //validate inputs
        //if is success
        //validate domain;
        reservationRepository.update(reservation, hostEmail, guestEmail);
        result.setPayload(reservation);

        return result;
    }


    public Result<Reservation> cancelReservation(Reservation reservation, String hostEmail, String guestEmail, int reservation_ID) throws DataException {

        Result<Reservation> result = new Result<>();
        validateEmail(hostEmail, result); //add if is succssc add more more to email validation, e.g. email format
        // I guess this part should run the finding of all res itself.
        //validate other inputs in reserv.
        if(!result.isSuccess()){
            return result;
        }
        validateEmail(guestEmail, result); // is success!
        if(!result.isSuccess()){
            return result;
        }

        Host host = hostRepository.findByEmail(hostEmail);
        List<Reservation> allGuestReservationsWithHost = findReservation(hostEmail, guestEmail);

        if (host == null) {
            result.addErrorMessage("Check host Email. No host with that email address exists.");
            return result;
        }

        validateNulls(reservation);

        if(!result.isSuccess()){
            return result;
        }

        validateFields(reservation, result);

        if(!result.isSuccess()){
            return result;
        }

        reservation.setHost(host);

        //val domain
        //check that the date are not taken res.start date  and end date do not over lapp any taken date already
        validateDomain(reservation, hostEmail, result);

        if(!result.isSuccess()){
            return result;
        }

        //*** validate guest Email
        validateEmail(guestEmail, result); // is success!

        Guest guest = guestRepository.findGuest(guestEmail);
        //if null
        if (guest == null) {
            result.addErrorMessage("Check guest email. No guest with that email address exists.");
            return result;
        } else reservation.setGuest(guest);

//        reservation.setGuestId(guest.getGuestID());

        //Here. It goes here!
        Reservation toBeDeleted =
                allGuestReservationsWithHost.stream().filter(reservation1 -> reservation1.getId() == reservation_ID)
                        .findFirst()
                        .orElse(null);
        if(toBeDeleted == null){
            result.addErrorMessage("Invalid reservation ID.");
            return result;
        }else reservation.setId(reservation_ID);

        //loop through the list of guest reservations with host. Get IDs. If reservation ID is not equal to any addErrMEs: Wrong ID inputted. Before setting it, actually


//        BigDecimal tot = new BigDecimal(0);
//        BigDecimal total;
//        LocalDate start = reservation.getStartDate();
//
//        for(start = start; start.getDayOfYear() < (reservation.getEndDate().getDayOfYear()) ; start = start.plusDays(1)){
//            if(start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY  ){
//                tot = tot.add(host.getWeekendRate());
//            }else tot = tot.add(host.getStandardRate());
//
//        }
//        reservation.setTotal(tot);
//        reservation.getStartDate().compareTo(reservation.getEndDate())
//        host.getStandardRate();


        //validate inputs
        //if is success
        //validate domain;
        reservationRepository.cancelReservation(reservation, hostEmail, guestEmail);
        result.setPayload(reservation);

        return result;
    }



    //************************ VALIDATION METHODS ******************************

    private Result<Reservation> validate(Reservation reservation) {

        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

//        validateChildrenExist(reservation, result);

        return result;
    }

    // **************** validating domain before adding a reservation *****************

    private void validateDomain(Reservation reservation, String hostEMail, Result<Reservation> result) throws DataException {

        List<Reservation> all = reservationRepository.findAllReservationsForHost(hostEMail);

        for (Reservation r : all) {
            if (reservation.getStartDate().isAfter(r.getStartDate()) && reservation.getStartDate().isBefore(r.getEndDate()) && reservation.getId() != r.getId()) {
                result.addErrorMessage("Reservation is overlapping reservation from " + r.getStartDate() + " - " + r.getEndDate());
            }
            if(reservation.getEndDate().isAfter(r.getStartDate()) && reservation.getEndDate().isBefore(r.getEndDate()) && reservation.getId() != r.getId()){
                result.addErrorMessage("Reservation is overlapping reservation from " + r.getStartDate() + " - " + r.getEndDate());
            }
            if(reservation.getStartDate().isEqual(r.getStartDate()) && reservation.getId() != r.getId()){
                result.addErrorMessage("Reservation Start date is colliding with reservation from " + r.getStartDate() + " - " + r.getEndDate());
            }
            if(reservation.getEndDate().isEqual(r.getEndDate()) && reservation.getId() != r.getId()){
                result.addErrorMessage("Reservation End date is colliding with reservation from " + r.getStartDate() + " - " + r.getEndDate());
            }
        }

    }


    //********** support validations ************

    private Result<Reservation> validateNulls(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Reservation start date is required.");
        }
        if (reservation.getEndDate() == null) {
            result.addErrorMessage("Reservation End date is required.");
        }
//id,start_date,end_date,guest_id,total

        return result;
    }

    private void validateFields(Reservation reservation, Result<Reservation> result) {
        // No future dates.
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation date cannot be in the past.");
        }
        if (reservation.getEndDate().isBefore(reservation.getStartDate())) {
            result.addErrorMessage("Reservation End date cannot be before reservation start date.");
        }

    }

//    private void validateChildrenExist(Reservation reservation, Result<Reservation> result) {
//
//        if (reservation.getReservationr().getId() == null
//                || reservationrRepository.findById(reservation.getReservationr().getId()) == null) {
//            result.addErrorMessage("Reservationr does not exist.");
//        }
//
//        if (itemRepository.findById(reservation.getItem().getId()) == null) {
//            result.addErrorMessage("Item does not exist.");
//        }
//    }

    private void validateEmail(String hostEmail, Result<Reservation> result) {
        if (hostEmail.trim().length() == 0) {
            result.addErrorMessage("Host Email cannot be empty.");
        }

    }
}
