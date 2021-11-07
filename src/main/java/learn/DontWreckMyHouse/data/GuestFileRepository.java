package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.models.Guest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository {

    private final String filepath;

    public GuestFileRepository(String filepath) {
        this.filepath = filepath;
    }

    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
