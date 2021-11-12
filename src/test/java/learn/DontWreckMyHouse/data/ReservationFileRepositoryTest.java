package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.models.Host;
import learn.DontWreckMyHouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {


    private static final String SEED_PATH = "./data/reservations/3edda6bc-ab95-49a8-8962-d50b53f84b15.csv";
    private static final String TEST_PATH = "./data/reservations-test/3edda6bc-ab95-49a8-8962-d50b53f84b15.csv";
    private final HostFileRepository hostFileRepository = new HostFileRepository("./data/hosts.csv");
    private final GuestFileRepository guestFileRepository = new GuestFileRepository("./data/guests.csv");
    private String directory = "data/reservations-test/";
    private ReservationFileRepository repository = new ReservationFileRepository(directory, hostFileRepository, guestFileRepository) ;
    private String hostID;
    private String getFilePath(String hostID) {
        return Paths.get(directory, hostID + ".csv").toString();
    }


    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH),
                Paths.get(TEST_PATH),
                StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void findAllReservationsForHost() {
        List<Reservation> allHostReservations = repository.findAllReservationsForHost("eyearnes0@sfgate.com");
        assertNotNull(allHostReservations);

    }

    @Test
    void findReservation() throws DataException, NoSuchElementException {
        String hostEmail = "eyearnes0@sfgate.com";
        String guestEmail = "dlynessy@icio.us";
        List<Reservation> allHostReservations = repository.findAllReservationsForHost(hostEmail);

        List<Reservation> findRes = repository.findReservation(hostEmail, guestEmail);

        assertNotNull(allHostReservations);
        assertNotNull(findRes);
//      assertEquals("Lyness", findRes.stream().findFirst().get().getGuest().getLastName());

    }
    @Test
    void notFindReservationForFalseEmails() throws DataException, NoSuchElementException {
        String hostEmail = "eyearnes0@sfgate.codfm";
        String guestEmail = "dlynessy@icio.usgg";
        List<Reservation> allHostReservations = repository.findAllReservationsForHost(hostEmail);

        List<Reservation> findRes = repository.findReservation(hostEmail, guestEmail);

        assertNull(allHostReservations);
        assertNull(findRes);
//      assertEquals("Lyness", findRes.stream().findFirst().get().getGuest().getLastName());

    }

//delete
    @Test
    void shouldDeleteExisting() throws DataException{
        directory = "data/reservations-test/";
        repository = new ReservationFileRepository(directory, hostFileRepository, guestFileRepository) ;

        String  hostEmail = "eyearnes0@sfgate.com";
        String guestEmail = "slomas0@mediafire.com";
        Host host = new Host();
        host.setLastName("Caesar");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("7739892149");
        host.setAddress("34 Joe Jacskon street");
        host.setCity("Hammond");
        host.setState("NV");
        host.setPostalCode(Integer.parseInt("56789"));
        host.setStandardRate(new BigDecimal("68.00"));
        host.setWeekendRate(new BigDecimal("75.00"));
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");

        Reservation result = new Reservation();

        result.setId(15);
        result.setHost(host);
        result.setStartDate(LocalDate.parse("2032-05-16") );
        result.setEndDate(LocalDate.parse("2032-05-23"));
        result.setGuestId(Integer.parseInt("30000") );
        result.setTotal(new BigDecimal("35000000") );

        repository.add(result, hostEmail, guestEmail);
        boolean actual = repository.cancelReservation(result, host.getEmail(), guestEmail);

        List<Reservation> allHost =   repository.findAllReservationsForHost(hostEmail);
        assertTrue(actual);


    }

    //Add

    @Test
    void shouldAddReservation() throws DataException {
        directory = "data/reservations-test/";
        repository = new ReservationFileRepository(directory, hostFileRepository, guestFileRepository) ;
        Host host = new Host();
        host.setLastName("Caesar");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhone("7739892149");
        host.setAddress("34 Joe Jacskon street");
        host.setCity("Hammond");
        host.setState("NV");
        host.setPostalCode(Integer.parseInt("56789"));
        host.setStandardRate(new BigDecimal("68.00"));
        host.setWeekendRate(new BigDecimal("75.00"));

        String hostEmail = host.getEmail();
        String guestEmail = "slomas0@mediafire.com";

        Reservation result = new Reservation();

        result.setStartDate(LocalDate.parse("2032-05-16") );
        result.setEndDate(LocalDate.parse("2032-05-23"));
        result.setGuestId(Integer.parseInt("30000") );
        result.setTotal(new BigDecimal("35000000") );

       Reservation res = repository.add(result, hostEmail, guestEmail);

        assertNotNull(res);
        assertEquals(15, res.getId());
    }

}