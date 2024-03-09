package ca.flymile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The FlyMileApplication class is the main entry point for the FlyMile application.
 */
@SpringBootApplication
public class FlyMileApplication extends SpringBootServletInitializer {

	/**
	 * The main method to start the FlyMile application.
	 *
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(FlyMileApplication.class, args);
	}

	/**
	 * Configure SpringApplicationBuilder for the FlyMile application.
	 *
	 * @param builder The SpringApplicationBuilder.
	 * @return The configured SpringApplicationBuilder.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FlyMileApplication.class);
	}
}
