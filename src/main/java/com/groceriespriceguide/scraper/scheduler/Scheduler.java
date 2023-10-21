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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class Scheduler {
    final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
    final Scraper scraper;
    final ProductService productService;

    public Scheduler(final Scraper scraper, final ProductService productService){
        this.scraper = scraper;
        this.productService = productService;
    }
    public static final int THOUSAND_SECONDS = 200000;
    @Scheduled(fixedDelay = THOUSAND_SECONDS)
    public void scheduleScraping() {
        List<Product> allProducts = null;
        try {
            allProducts = scraper.scrapeProducts();
        } catch (final Exception e) {
            LOGGER.error("error occurred while scraping data", e);
        }
    if (allProducts  !=  null && !allProducts.isEmpty()) {
        List<Product> productsToUpdate = new ArrayList<>();
        List<Product> productsToAdd = new ArrayList<>();
        for (Product product : allProducts) {
            if (product != null) {
                String url = product.getProductUrl();
                Product existingProduct = productService.getProductByURL(url);
                if (existingProduct != null) {
                    // A product with the same URL already exists, update its price and lastUpdatedAt
                    existingProduct.setProductPrice(product.getProductPrice());
                    existingProduct.setLastUpdated(new Timestamp(System.currentTimeMillis()));
                    productsToUpdate.add(existingProduct);
                } else {
                    // The product does not exist, persist it
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    product.setLastUpdated(currentTimestamp);
                    product.setCreatedAT(currentTimestamp);
                    productsToAdd.add(product);
                }
            }
        }
        // Update existing products one by one
        for (Product productToUpdate : productsToUpdate) {
            productService.updateExistingProduct(productToUpdate);

        }

        // Remove updated products from the original list
       if (productsToAdd != null) productService.persistProduct(productsToAdd);
    }
}
}
