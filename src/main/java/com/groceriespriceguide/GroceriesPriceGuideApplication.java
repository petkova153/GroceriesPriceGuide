package com.groceriespriceguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {"com.groceriespriceguide.products.entity"})
@ComponentScan({ "com.groceriespriceguide.scraper.scheduler",
		"com.groceriespriceguide.scraper.scraper",
		"com.groceriespriceguide.products.controller",
		"com.groceriespriceguide.products.services.impl"
})
@EnableJpaRepositories(basePackages = "com.groceriespriceguide.products.repository")
public class GroceriesPriceGuideApplication {

	public static void main(String[] args) {

		SpringApplication.run(GroceriesPriceGuideApplication.class, args);
	}

}
