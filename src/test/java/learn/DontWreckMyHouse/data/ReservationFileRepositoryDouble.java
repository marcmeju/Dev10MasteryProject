package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.data.*;
import learn.DontWreckMyHouse.models.Guest;
import learn.DontWreckMyHouse.models.Host;
import learn.DontWreckMyHouse.models.Reservation;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationFileRepositoryDouble implements ReservationRepository {

    public ReservationFileRepositoryDouble(){
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

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setGuestId(1);
        reservation.setStartDate(LocalDate.parse("2022-06-11"));
        reservation.setEndDate(LocalDate.parse("2022-06-18"));
        reservation.setTotal(new BigDecimal("5500"));
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservations.add(reservation);

    }
     ArrayList<Reservation> reservations = new ArrayList<>();

    private final String HEADER = "id,start_date,end_date,guest_id,total";

    @Override
    public List<Reservation> findAllReservationsForHost(String hostEmail) {

        return reservations;
    }

    @Override
    public List<Reservation> findReservation(String hostEmail, String guestEmail) throws DataException {

return reservations;
    }



    @Override
    public Reservation add(Reservation reservation, String hostEmail, String guestEmail) throws DataException {
       return null;
    }

    @Override
    public boolean update(Reservation reservation, String hostEmail, String guestEmail) {
        return true;
    }

    @Override
    public boolean cancelReservation(Reservation reservation, String hostEmail, String guestEmail) {
        return true;
    }

    //Support

    private void writeAll(List<Reservation> reservations, String hostID) throws DataException {
        try (PrintWriter writer = new PrintWriter("./data/reservations-test/reservation-test.csv")) {

            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
               reservation.getGuestId(),
                reservation.getTotal());
    }

    private Reservation deserialize(String[] fields){
        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]) );
        result.setStartDate(LocalDate.parse(fields[1]) );
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setGuestId(Integer.parseInt(fields[3]) );
        result.setTotal(new BigDecimal(fields[4]) );

        return result;
//"id,start_date,end_date,guest_id,total"
    }
}
