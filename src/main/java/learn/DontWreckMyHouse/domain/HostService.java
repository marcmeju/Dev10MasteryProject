package learn.DontWreckMyHouse.domain;

import learn.DontWreckMyHouse.data.DataException;
import learn.DontWreckMyHouse.data.HostRepository;
import learn.DontWreckMyHouse.models.Host;

import java.io.FileNotFoundException;
import java.util.List;

public class HostService {
    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    //**************** METHODS *********************
    //**********************************************

    //***************findAll()********************
    public List<Host> findAll() throws FileNotFoundException {
        return repository.findAll();
    }

    //*************** find by Email **************
    public Host findByEmail(String email){
        //validate inputs.
        //validate domain. not really
        return repository.findByEmail(email);
    }

   //************* add Host() ********************
    public Host add(Host host) throws DataException{
        //validate inputs
        //if isSuccess, then
        //validate domain;
        //if isSuccess, then
        return repository.add(host);
    }




    //******************** VALIDATION METHODS***************************


}
