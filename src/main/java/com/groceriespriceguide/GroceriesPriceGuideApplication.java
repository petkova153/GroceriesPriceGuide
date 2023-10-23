package com.groceriespriceguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = {"com.groceriespriceguide.entity"})
@ComponentScan({"com.groceriespriceguide.scraper.scheduler",
        "com.groceriespriceguide.scraper.scraper",
        "com.groceriespriceguide.controller",
        "com.groceriespriceguide.services.impl"
})
@EnableJpaRepositories(basePackages = "com.groceriespriceguide.repository")
public class GroceriesPriceGuideApplication {

    public static void main(String[] args) {

        SpringApplication.run(GroceriesPriceGuideApplication.class, args);
    }
}
