package learn.DontWreckMyHouse.data;

import learn.DontWreckMyHouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    private static final String SEED_PATH = "./data/hosts-seed.csv";
    private static final String TEST_PATH = "./data/hosts-test.csv";
    private HostFileRepository repository = new HostFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH),
                Paths.get(TEST_PATH),
                StandardCopyOption.REPLACE_EXISTING);
    }

//FindAll
@Test
    void shouldFindAll(){
        List<Host> result = repository.findAll();
        assertNotNull(result);
        assertEquals(5, result.size());
}

@Test
void shouldNotFindInEmptyList(){
    HostFileRepository repository = new HostFileRepository("./data/empty.csv");
    List<Host> result = repository.findAll();
    assertEquals(0, result.size());
}

//Add
    @Test
    void shouldAdd() throws DataException {
        Host result = new Host();

        result.setLastName("Caesar");
        result.setEmail("juliocaesar@romanempire.com");
        result.setPhone("7739892149");
        result.setAddress("34 Joe Jacskon street");
        result.setCity("Hammond");
        result.setState("NV");
        result.setPostalCode(Integer.parseInt("56789"));
        result.setStandardRate(new BigDecimal(68.00));
        result.setWeekendRate(new BigDecimal(75.00));

         repository.add(result);

         assertNotNull(result);
         assertEquals(6, repository.findAll().size());
    }

    // FindBYEmail

    @Test
    void shouldFindByEmail(){
        Host result = new Host();
        String email = "charley4@apple.com";
        result = repository.findByEmail(email);

        assertNotNull(result);
       assertEquals("Harley", result.getLastName());
    }

    @Test
    void shouldNotFindFalseEmail(){
        Host result = new Host();
        String email = "charley4@appwsfwefe.com";
        result = repository.findByEmail(email);

       assertNull(result);

    }
}