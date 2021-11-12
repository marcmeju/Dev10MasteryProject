package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.data.DataException;
import learn.DontWreckMyHouse.data.HostRepository;
import learn.DontWreckMyHouse.models.Host;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepositoryDouble implements HostRepository {

    public HostFileRepositoryDouble(){
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
hosts.add(host);

    }
    private ArrayList<Host> hosts = new ArrayList<>();
    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";

    @Override
    public List<Host> findAll()  {
        return new ArrayList<>(hosts);

    }

    @Override
    public Host findByEmail(String email) {
        return findAll().stream()
                .filter(host -> host.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

    }

    @Override
    public Host add(Host host) throws DataException {
        List<Host> all = findAll();
        host.setId(java.util.UUID.randomUUID().toString());
        all.add(host);

        writeAll(all);

        return host;
    }

    @Override
    public boolean update(Host host) {
        return false;
    }

    @Override
    public boolean deleteByEmail(String email) {
        return false;
    }


    //support
    private Host deserialize(String[] fields){
Host result = new Host();
result.setId(fields[0]);
result.setLastName(fields[1]);
result.setEmail(fields[2]);
result.setPhone(fields[3]);
result.setAddress(fields[4]);
result.setCity(fields[5]);
result.setState(fields[6]);
result.setPostalCode(Integer.parseInt(fields[7]));
result.setStandardRate(new BigDecimal(fields[8]));
result.setWeekendRate(new BigDecimal(fields[9]));

return result;
//id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate
    }

    private void writeAll(List<Host> hosts) throws DataException {
        try(PrintWriter writer = new PrintWriter("./data/reservations-test/empty.csv")){

            writer.println(HEADER);

            for (Host host : hosts){
                writer.println(serialize(host));
            }

        }catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Host host){

        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
        host.getId(),
        host.getLastName(),
        host.getEmail(),
        host.getPhone(), //add the brackets
        host.getAddress(),
        host.getCity(),
        host.getState(),
        host.getPostalCode(),
        host.getStandardRate(),
        host.getWeekendRate());
    }
}
