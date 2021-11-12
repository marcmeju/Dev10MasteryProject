package learn.DontWreckMyHouse.data;

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

public class ReservationFileRepository implements ReservationRepository {
    private final String directory;
    private final HostFileRepository hostFileRepository;
    private final GuestFileRepository guestFileRepository;

    public ReservationFileRepository(String directory, HostFileRepository hostFileRepository, GuestFileRepository guestFileRepository) {
        this.directory = directory;
        this.hostFileRepository = hostFileRepository;
        this.guestFileRepository = guestFileRepository;
    }

    String hostID;
    int guestID;
    private final String HEADER = "id,start_date,end_date,guest_id,total";

    private String getFilePath(String hostID) {
        return Paths.get(directory, hostID + ".csv").toString();
    }

    @Override
    public List<Reservation> findAllReservationsForHost(String hostEmail) {
        Host host = hostFileRepository.findByEmail(hostEmail);
        if (host != null) {
            hostID = host.getId();
        } else return null;

        List<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostID)))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Reservation> findReservation(String hostEmail, String guestEmail) throws DataException {
        List<Reservation> allHostReservations = findAllReservationsForHost(hostEmail);
        //scan through guestfile for email
        Guest guest = guestFileRepository.findGuest(guestEmail);
        if (guest != null) {
            guestID = guest.getGuestID();
        } else return null;
        return allHostReservations.stream()
                .filter(reservation -> reservation.getGuestId() == guestID)
                .sorted(Comparator.comparing(Reservation::getStartDate).reversed())
                .collect(Collectors.toList());

    }

    @Override
    public Reservation add(Reservation reservation, String hostEmail, String guestEmail) throws DataException {
//        Host host = new Host();
        Host host = hostFileRepository.findByEmail(hostEmail);

        List<Reservation> all = findAllReservationsForHost(hostEmail);

        Guest guest = guestFileRepository.findGuest(guestEmail);

        int nextId = 0;
        for (Reservation r : all) {
            nextId = Math.max(nextId, r.getId());
        }
        nextId += 1;
        reservation.setId(nextId);

            all.add(reservation);

        writeAll(all, host.getId());

        return reservation;
    }

    @Override
    public boolean update(Reservation reservation, String hostEmail, String guestEmail) throws DataException {
        List<Reservation> allGuestReservationsWithHost = findReservation(hostEmail, guestEmail);
        List<Reservation> all = findAllReservationsForHost(hostEmail);
        //print to console here for user to see
        //then ask for  and set reservation id
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == (reservation.getId())) {
                all.set(i, reservation);
                writeAll(all, reservation.getHost().getId());
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean cancelReservation(Reservation reservation, String hostEmail, String guestEmail) throws DataException {
        List<Reservation> allGuestReservationsWithHost = findReservation(hostEmail, guestEmail);
        List<Reservation> all = findAllReservationsForHost(hostEmail);

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.remove(i);
                writeAll(all, reservation.getHost().getId());
                return true;
            }
        }

        return false;
    }


    //***************** Support Methods {Serialize, Deserialize, Write All } ******************************

    private void writeAll(List<Reservation> reservations, String hostID) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostID))) {

            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
//            throw new DataException(ex);
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

    private Reservation deserialize(String[] fields) {
        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]));
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setGuestId(Integer.parseInt(fields[3]));
        result.setTotal(new BigDecimal(fields[4]));

        return result;
        //"id,start_date,end_date,guest_id,total"
    }
}
