package verzelEvents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VerzelEventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerzelEventsApplication.class, args);
	}
}