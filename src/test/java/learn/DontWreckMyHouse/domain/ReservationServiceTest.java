package learn.DontWreckMyHouse.domain;

import learn.DontWreckMyHouse.data.*;
import learn.DontWreckMyHouse.models.Guest;
import learn.DontWreckMyHouse.models.Host;
import learn.DontWreckMyHouse.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

//    private final ReservationFileRepositoryDouble reservationFileRepositoryDouble;
//    private final GuestFileRepositoryDouble guestFileRepositoryDouble;
//    private final HostFileRepositoryDouble hostFileRepositoryDouble;

    ReservationService service = new ReservationService( new ReservationFileRepositoryDouble(), new GuestFileRepositoryDouble(), new HostFileRepositoryDouble());

    @Test
    void findAllReservationsForHost() throws DataException {
       List<Reservation> reservation = service.findAllReservationsForHost("eyearnes0@sfgate.com");
       assertEquals(1, reservation.size());
    }

    @Test
    void findReservation() throws DataException {
        List<Reservation> reservation =  service.findReservation("eyearnes0@sfgate.com", "slomas0@mediafire.com");
        assertEquals(1, reservation.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setGuestId(1);
        reservation.setStartDate(LocalDate.parse("2022-06-11"));
        reservation.setEndDate(LocalDate.parse("2022-06-18"));
        reservation.setTotal(new BigDecimal("5500"));

        Host host = new Host();
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("8061783815");
        host.setAddress("3 Nova Trail");
        host.setCity("Amarillo");
        host.setState("TX");
        host.setPostalCode(79182);
        host.setStandardRate(new BigDecimal(340));
        host.setWeekendRate(new BigDecimal(425));

        Guest guest = new Guest();
        guest.setGuestID(1);
        guest.setLastName("Lomas");
        guest.setFirstName("Sullivan");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhone("7027768761");
        guest.setState("NV");

       Result<Reservation> result = service.add(reservation, host.getEmail(), guest.getEmail());
       assertTrue(result.isSuccess());
    }

@Test
    void shouldNotAddWithIncompleteReservationField() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setGuestId(1);
        reservation.setStartDate(LocalDate.parse("2022-06-11"));
//        reservation.setEndDate(LocalDate.parse("2022-06-18"));
        reservation.setTotal(new BigDecimal("5500"));

        Host host = new Host();
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("8061783815");
        host.setAddress("3 Nova Trail");
        host.setCity("Amarillo");
        host.setState("TX");
        host.setPostalCode(79182);
        host.setStandardRate(new BigDecimal(340));
        host.setWeekendRate(new BigDecimal(425));

        Guest guest = new Guest();
        guest.setGuestID(1);
        guest.setLastName("Lomas");
        guest.setFirstName("Sullivan");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhone("7027768761");
        guest.setState("NV");

        Result<Reservation> result = service.add(reservation, host.getEmail(), guest.getEmail());
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithNoGuestId() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
//        reservation.setGuestId(1);
        reservation.setStartDate(LocalDate.parse("2022-06-11"));
        reservation.setEndDate(LocalDate.parse("2022-06-18"));


        Host host = new Host();
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("8061783815");
        host.setAddress("3 Nova Trail");
        host.setCity("Amarillo");
        host.setState("TX");
        host.setPostalCode(79182);
        host.setStandardRate(new BigDecimal(340));
        host.setWeekendRate(new BigDecimal(425));

        Guest guest = new Guest();
        guest.setGuestID(1);
        guest.setLastName("Lomas");
        guest.setFirstName("Sullivan");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhone("7027768761");
        guest.setState("NV");

        Result<Reservation> result = service.add(reservation, host.getEmail(), guest.getEmail());
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithNoGuestEmail() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
//        reservation.setGuestId(1);
        reservation.setStartDate(LocalDate.parse("2022-06-11"));
        reservation.setEndDate(LocalDate.parse("2022-06-18"));


        Host host = new Host();
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("8061783815");
        host.setAddress("3 Nova Trail");
        host.setCity("Amarillo");
        host.setState("TX");
        host.setPostalCode(79182);
        host.setStandardRate(new BigDecimal(340));
        host.setWeekendRate(new BigDecimal(425));

        Guest guest = new Guest();
        guest.setGuestID(1);
        guest.setLastName("Lomas");
        guest.setFirstName("Sullivan");

        guest.setPhone("7027768761");
        guest.setState("NV");

        Result<Reservation> result = service.add(reservation, host.getEmail(), guest.getEmail());
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldUpdate() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setGuestId(1);
        reservation.setStartDate(LocalDate.parse("2022-05-11"));
        reservation.setEndDate(LocalDate.parse("2022-06-18"));
        reservation.setTotal(new BigDecimal("5500"));

        Host host = new Host();
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("8061783815");
        host.setAddress("3 Nova Trail");
        host.setCity("Amarillo");
        host.setState("TX");
        host.setPostalCode(79182);
        host.setStandardRate(new BigDecimal(340));
        host.setWeekendRate(new BigDecimal(425));

        Guest guest = new Guest();
        guest.setGuestID(1);
        guest.setLastName("Lomas");
        guest.setFirstName("Sullivan");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhone("7027768761");
        guest.setState("NV");

        Result<Reservation> result = service.update(reservation, host.getEmail(), guest.getEmail(), reservation.getId());
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNonExisting() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1000);
        reservation.setGuestId(1);
        reservation.setStartDate(LocalDate.parse("2022-05-11"));
        reservation.setEndDate(LocalDate.parse("2022-06-18"));
        reservation.setTotal(new BigDecimal("5500"));

        Host host = new Host();
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("8061783815");
        host.setAddress("3 Nova Trail");
        host.setCity("Amarillo");
        host.setState("TX");
        host.setPostalCode(79182);
        host.setStandardRate(new BigDecimal(340));
        host.setWeekendRate(new BigDecimal(425));

        Guest guest = new Guest();
        guest.setGuestID(1);
        guest.setLastName("Lomas");
        guest.setFirstName("Sullivan");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhone("7027768761");
        guest.setState("NV");

        Result<Reservation> result = service.update(reservation, host.getEmail(), guest.getEmail(), reservation.getId());
        assertFalse(result.isSuccess());
    }

@Test
    void shouldDeleteReservation() throws DataException {
    Reservation reservation = new Reservation();
    reservation.setId(1);
    reservation.setGuestId(1);
    reservation.setStartDate(LocalDate.parse("2022-05-11"));
    reservation.setEndDate(LocalDate.parse("2022-06-18"));
    reservation.setTotal(new BigDecimal("5500"));

    Host host = new Host();
    host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
    host.setEmail("eyearnes0@sfgate.com");
    host.setPhone("8061783815");
    host.setAddress("3 Nova Trail");
    host.setCity("Amarillo");
    host.setState("TX");
    host.setPostalCode(79182);
    host.setStandardRate(new BigDecimal(340));
    host.setWeekendRate(new BigDecimal(425));

    Guest guest = new Guest();
    guest.setGuestID(1);
    guest.setLastName("Lomas");
    guest.setFirstName("Sullivan");
    guest.setEmail("slomas0@mediafire.com");
    guest.setPhone("7027768761");
    guest.setState("NV");

    Result<Reservation> result =   service.cancelReservation(reservation, host.getEmail(), guest.getEmail(), reservation.getId());
    assertTrue(result.isSuccess());
}

    @Test
    void shouldNotDeleteNonExistingReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(1000);
        reservation.setGuestId(1);
        reservation.setStartDate(LocalDate.parse("2022-05-11"));
        reservation.setEndDate(LocalDate.parse("2022-06-18"));
        reservation.setTotal(new BigDecimal("5500"));

        Host host = new Host();
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("8061783815");
        host.setAddress("3 Nova Trail");
        host.setCity("Amarillo");
        host.setState("TX");
        host.setPostalCode(79182);
        host.setStandardRate(new BigDecimal(340));
        host.setWeekendRate(new BigDecimal(425));

        Guest guest = new Guest();
        guest.setGuestID(1);
        guest.setLastName("Lomas");
        guest.setFirstName("Sullivan");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhone("7027768761");
        guest.setState("NV");

        Result<Reservation> result =   service.cancelReservation(reservation, host.getEmail(), guest.getEmail(), reservation.getId());
        assertFalse(result.isSuccess());
    }
}