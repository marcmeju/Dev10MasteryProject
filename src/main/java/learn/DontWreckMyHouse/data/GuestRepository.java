package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll(); //Done.
    Guest findGuest(String email) throws DataException; //Done.

    //next is reservations.

}
