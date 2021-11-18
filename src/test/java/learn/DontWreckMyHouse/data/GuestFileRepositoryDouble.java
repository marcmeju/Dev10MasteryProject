package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.data.DataException;
import learn.DontWreckMyHouse.data.GuestRepository;
import learn.DontWreckMyHouse.models.Guest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepositoryDouble implements GuestRepository {



    public GuestFileRepositoryDouble() {
        Guest guest = new Guest();
        guest.setGuestID(1);
        guest.setLastName("Lomas");
        guest.setFirstName("Sullivan");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhone("7027768761");
        guest.setState("NV");

        guests.add(guest);

    }
private ArrayList<Guest> guests = new ArrayList<>();
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";

    @Override
    public List<Guest> findAll() {

        return new ArrayList<>(guests);
    }

    @Override
    public Guest findGuest(String email) throws DataException {
        return findAll().stream()
                .filter(guest -> guest.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }


    //support
    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setGuestID(Integer.parseInt(fields[0]));
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone((fields[4]));
        result.setState(fields[5]);

        return result;
//guest_id,first_name,last_name,email,phone,state
    }
}
