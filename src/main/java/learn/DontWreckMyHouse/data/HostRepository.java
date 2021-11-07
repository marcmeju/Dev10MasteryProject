package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.models.Host;

import java.io.FileNotFoundException;
import java.util.List;

public interface HostRepository {

    List<Host> findAll() throws FileNotFoundException; //done, tested, works.

    Host findByEmail(String email); //done, tested, works.

    Host add(Host host) throws DataException; // done, tested, works.

    boolean update(Host host);

    boolean deleteByEmail(String email);

}
