package com.groceriespriceguide.scraper.scheduler;

import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.services.ProductService;
import com.groceriespriceguide.scraper.scraper.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    if (allProducts  !=  null && !allProducts.isEmpty()) {
        int productIndex = 0;
        for (Product product : allProducts) {
            if (product != null) {
                String url = product.getProductUrl();
                Product existingProduct = productService.getProductByURL(url);
                productIndex++;
                if (existingProduct != null) {
                    // A product with the same URL already exists, update its price and lastUpdatedAt
                    existingProduct.setProductPrice(product.getProductPrice());
                    existingProduct.setLastUpdated(new Timestamp(System.currentTimeMillis()));
                    productService.updateExistingProduct(existingProduct);
                    allProducts.remove(productIndex);
                } else {
                    // The product does not exist, persist it
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    product.setLastUpdated(currentTimestamp);
                    product.setCreatedAT(currentTimestamp);
                }
            }
        }
        productService.persistProduct(allProducts);
    }
}
}
