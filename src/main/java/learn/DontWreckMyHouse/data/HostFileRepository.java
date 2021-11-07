package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.models.Host;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository{
    private final String filepath;
    public HostFileRepository(String filepath){this.filepath = filepath;}

    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";

    @Override
    public List<Host> findAll()  {
        ArrayList<Host> result = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filepath))){

            reader.readLine();

            for(String line = reader.readLine(); line != null; line = reader.readLine()){
                String[] fields = line.split(",", -1);
                if(fields.length == 10){
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
        try(PrintWriter writer = new PrintWriter(filepath)){

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
