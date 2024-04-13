package ca.flymile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * The FlyMileApplication class is the main entry point for the FlyMile application.
 */
@SpringBootApplication(exclude={RedisAutoConfiguration.class})
@OpenAPIDefinition(info = @Info(title = "API Doc for FlyMile.ca", version="1.0.0", description = "FlyMile API provides access to flight information and points redemption options for users to elevate their travel experience and maximize the value of their loyalty points. By leveraging data scraped from online airline websites, users can search for superior flight options and make informed decisions about redeeming their points."))
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
