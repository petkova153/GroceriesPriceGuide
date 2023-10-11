package com.groceriespriceguide.scraper.scheduler;

import com.groceriespriceguide.products.entity.Product;
import com.groceriespriceguide.products.services.ProductService;
import com.groceriespriceguide.scraper.scraper.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Component
public class Scheduler {
    final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    Scraper scraper;
    @Autowired
    ProductService productService;
    public static final int THOUSAND_SECONDS = 1000000;
    @Scheduled(fixedDelay = THOUSAND_SECONDS)
    public void scheduleScraping() {
        List<Product> allProducts = null;
        try {
            allProducts = scraper.scrapeProducts();
        } catch (final Exception e) {
            LOGGER.error("error occurred while scraping data", e);
        }
    if (null != allProducts && !allProducts.isEmpty()) {
        productService.persistProduct(allProducts);
    }
}
}
