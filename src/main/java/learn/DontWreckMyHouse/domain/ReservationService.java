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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        Result result = new Result();

        return reservationRepository.findAllReservationsForHost(hostEmail).stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }


    //************************ FIND RESERVATION(S) *******************************
    public List<Reservation> findReservation(String hostEmail, String guestEmail) throws DataException {

        return reservationRepository.findReservation(hostEmail, guestEmail).stream()
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());
    }


    //************************* ADD A RESERVATION ****************************
    public Result<Reservation> add(Reservation reservation, String hostEmail, String guestEmail) throws DataException {

        Result<Reservation> result = new Result<>();

        validateEmail(hostEmail, result);
        if (!result.isSuccess()) {
            return result;
        }
        Host host = hostRepository.findByEmail(hostEmail);
        if (host == null) {
            result.addErrorMessage("Check host Email. No host with that email address exists.");
            return result;
        }

        validateNulls(reservation, result);

        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);

        if (!result.isSuccess()) {
            return result;
        }
        reservation.setHost(host);

        validateDomain(reservation, hostEmail, result);

        if (!result.isSuccess()) {
            return result;
        }

        validateEmail(guestEmail, result); // is success!

        Guest guest = guestRepository.findGuest(guestEmail);

        if (guest == null) {
            result.addErrorMessage("Check guest email. No guest with that email address exists.");
            return result;
        } else reservation.setGuest(guest);

        reservation.setGuestId(guest.getGuestID());
        setTotal(reservation, host);
//        validateTotal(setTotal(reservation, host), result);
//        if (!result.isSuccess()) {
//            return result;
//        }

        result.setPayload(reservationRepository.add(reservation, hostEmail, guestEmail));

        return result;
    }



    //************************* UPDATE A RESERVATION ****************************
    public Result<Reservation> update(Reservation reservation, String hostEmail, String guestEmail, int reservation_ID) throws DataException {

        Result<Reservation> result = new Result<>();
        validateEmail(hostEmail, result);

        if (!result.isSuccess()) {
            return result;
        }
        validateEmail(guestEmail, result);
        if (!result.isSuccess()) {
            return result;
        }

        Host host = hostRepository.findByEmail(hostEmail);
        List<Reservation> allGuestReservationsWithHost = findReservation(hostEmail, guestEmail);

        if (host == null) {
            result.addErrorMessage("Check host Email. No host with that email address exists.");
            return result;
        }

        validateNulls(reservation, result);

        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);

        if (!result.isSuccess()) {
            return result;
        }

        reservation.setHost(host);

        validateEmail(guestEmail, result);

        Guest guest = guestRepository.findGuest(guestEmail);

        if (guest == null) {
            result.addErrorMessage("Check guest email. No guest with that email address exists.");
            return result;
        } else reservation.setGuest(guest);

        reservation.setGuestId(guest.getGuestID());

        Reservation toBeUpdated =
                allGuestReservationsWithHost.stream().filter(reservation1 -> reservation1.getId() == reservation_ID)
                        .findFirst()
                        .orElse(null);
        if (toBeUpdated == null) {
            result.addErrorMessage("Invalid reservation ID.");
            return result;
        } else reservation.setId(reservation_ID);

        setTotal(reservation, host);

        validateDomain(reservation, hostEmail, result);

        if (!result.isSuccess()) {
            return result;
        }

        reservationRepository.update(reservation, hostEmail, guestEmail);
        result.setPayload(reservation);

        return result;
    }

    //************************* DELETE A RESERVATION ****************************
    public Result<Reservation> cancelReservation(Reservation reservation, String hostEmail, String guestEmail, int reservation_ID) throws DataException {

        Result<Reservation> result = new Result<>();
        validateEmail(hostEmail, result);
        if (!result.isSuccess()) {
            return result;
        }
        validateEmail(guestEmail, result);
        if (!result.isSuccess()) {
            return result;
        }

        Host host = hostRepository.findByEmail(hostEmail);
        List<Reservation> allGuestReservationsWithHost = findReservation(hostEmail, guestEmail);

        if (host == null) {
            result.addErrorMessage("Check host Email. No host with that email address exists.");
            return result;
        }

        validateNulls(reservation, result);

        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);

        if (!result.isSuccess()) {
            return result;
        }

        reservation.setHost(host);

        validateDomain(reservation, hostEmail, result);

        if (!result.isSuccess()) {
            return result;
        }

        validateEmail(guestEmail, result);

        Guest guest = guestRepository.findGuest(guestEmail);
        //if null
        if (guest == null) {
            result.addErrorMessage("Check guest email. No guest with that email address exists.");
            return result;
        } else reservation.setGuest(guest);

        Reservation toBeDeleted =
                allGuestReservationsWithHost.stream().filter(reservation1 -> reservation1.getId() == reservation_ID)
                        .findFirst()
                        .orElse(null);
        if (toBeDeleted == null) {
            result.addErrorMessage("Invalid reservation ID.");
            return result;
        } else reservation.setId(reservation_ID);

        reservationRepository.cancelReservation(reservation, hostEmail, guestEmail);
        result.setPayload(reservation);

        return result;
    }

    //************************ Support for CRUD Methods ******************************
    //************************ Set Reservation Total ******************************
    public BigDecimal setTotal(Reservation reservation, Host host) {
        BigDecimal tot = new BigDecimal(0);
        BigDecimal total;
        LocalDate start = reservation.getStartDate();

        for (start = start; start.getDayOfYear() < (reservation.getEndDate().getDayOfYear()); start = start.plusDays(1)) {
            if (start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY) {
                tot = tot.add(host.getWeekendRate());
            } else tot = tot.add(host.getStandardRate());

        }
         reservation.setTotal(tot);
        return tot;
    }


    //************************ VALIDATION METHODS ******************************
    //**************************************************************************

    //************************ Validate Reservation ******************************
    private Result<Reservation> validate(Reservation reservation) {
        Result<Reservation> result = new Result<>();
        validateNulls(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }
        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }
        return result;
    }


    // **************** validating domain before adding a reservation *****************

    private void validateDomain(Reservation reservation, String hostEMail, Result<Reservation> result) throws DataException {

        List<Reservation> all = reservationRepository.findAllReservationsForHost(hostEMail);

        for (Reservation r : all) {
            if (reservation.getStartDate().isAfter(r.getStartDate()) && reservation.getStartDate().isBefore(r.getEndDate()) && reservation.getId() != r.getId()) {
                result.addErrorMessage("Reservation is overlapping reservation from " + r.getStartDate() + " - " + r.getEndDate());
            }
            if (reservation.getEndDate().isAfter(r.getStartDate()) && reservation.getEndDate().isBefore(r.getEndDate()) && reservation.getId() != r.getId()) {
                result.addErrorMessage("Reservation is overlapping reservation from " + r.getStartDate() + " - " + r.getEndDate());
            }
            if (reservation.getStartDate().isEqual(r.getStartDate()) && reservation.getGuestId() != r.getGuestId()) {
                result.addErrorMessage("Reservation Start date is colliding with reservation from " + r.getStartDate() + " - " + r.getEndDate());
            }
            if (reservation.getEndDate().isEqual(r.getEndDate()) && reservation.getGuestId() != r.getGuestId()) {
                result.addErrorMessage("Reservation End date is colliding with reservation from " + r.getStartDate() + " - " + r.getEndDate());
            }
        }

    }


    //********** support validations ************

    private Result<Reservation> validateNulls(Reservation reservation, Result<Reservation> result) {
//        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Nothing to save.");
            return result;
        }

        if (reservation.getStartDate() == null || reservation.getStartDate().toString().isBlank()) {
            result.addErrorMessage("Reservation start date is required.");
        }
        if (reservation.getEndDate() == null || reservation.getEndDate().toString().isBlank()) {
            result.addErrorMessage("Reservation End date is required.");
        }
//id,start_date,end_date,guest_id,total

        return result;
    }

    //************************ Validate fields ******************************
    private void validateFields(Reservation reservation, Result<Reservation> result) {
        // No future dates.
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation date cannot be in the past.");
        }
        if (reservation.getEndDate().isBefore(reservation.getStartDate())) {
            result.addErrorMessage("Reservation End date cannot be before reservation start date.");
        }

    }

    //************************ Validate Email ******************************
    private void validateEmail(String hostEmail, Result<Reservation> result) {
        if (hostEmail.trim().length() == 0) {
            result.addErrorMessage("Host Email cannot be empty.");
        }

    }

//    private void validateTotal(BigDecimal total, Result<Reservation> result) {
//        if (total.equals(new BigDecimal(0)) || total == null) {
//            result.addErrorMessage("Total cannot be zero.");
//        }
//
//    }
}
