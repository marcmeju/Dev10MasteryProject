package learn.DontWreckMyHouse.domain;

import learn.DontWreckMyHouse.data.GuestRepository;
import learn.DontWreckMyHouse.models.Guest;

import java.util.List;

public class GuestService {
    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    //************ METHODS *************
    public List<Guest> findAll(){
        return repository.findAll();
    }




    //************ VALIDATION METHODS **************


}
