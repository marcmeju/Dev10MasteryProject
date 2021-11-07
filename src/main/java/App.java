import learn.DontWreckMyHouse.data.GuestFileRepository;
import learn.DontWreckMyHouse.data.HostFileRepository;
import learn.DontWreckMyHouse.data.ReservationFileRepository;
import learn.DontWreckMyHouse.domain.GuestService;
import learn.DontWreckMyHouse.domain.HostService;
import learn.DontWreckMyHouse.domain.ReservationService;
import learn.DontWreckMyHouse.ui.ConsoleIO;
import learn.DontWreckMyHouse.ui.Controller;
import learn.DontWreckMyHouse.ui.View;

public class App {

    public static void main(String[] args) {

        ConsoleIO io = new ConsoleIO();
        View view = new View(io);

        GuestFileRepository guestFileRepository = new GuestFileRepository("./data/guests.csv");
        HostFileRepository hostFileRepository = new HostFileRepository("./data/hosts.csv");
        ReservationFileRepository reservationFileRepository = new ReservationFileRepository("./data/reservations", hostFileRepository, guestFileRepository);

        GuestService guestService = new GuestService(guestFileRepository);
        ReservationService reservationService = new ReservationService(reservationFileRepository, guestFileRepository, hostFileRepository);
        HostService hostService = new HostService(hostFileRepository);


//        ApplicationContext context = new ClassPathXmlApplicationContext("reservations-config.xml");
//        Controller controller= context.getBean(Controller.class);
//        controller.run();

        Controller controller = new Controller( hostService, guestService, reservationService, view);
        controller.run();
    }

}
