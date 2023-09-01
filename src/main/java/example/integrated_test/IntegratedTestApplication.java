package example.integrated_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IntegratedTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(IntegratedTestApplication.class, args);
	}
}
