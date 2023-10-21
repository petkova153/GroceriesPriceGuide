package com.groceriespriceguide.scraper.scheduler;

import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.scraper.scraper.*;
import com.groceriespriceguide.services.ProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;

@Component
public class Scheduler {
    final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
    final Scraper scraper;
    final ProductService productService;
    final BarboraScraper barboraScraper;
    final RimiScraper rimiScraper;
    final IkiScraper ikiScraper;
    final AssortiScraper assortiScraper;

    public Scheduler(final Scraper scraper, final ProductService productService,final BarboraScraper barboraScraper, final RimiScraper rimiScraper,
                     final IkiScraper ikiScraper, final AssortiScraper assortiScraper){
        this.barboraScraper = barboraScraper;
        this.rimiScraper = rimiScraper;
        this.ikiScraper = ikiScraper;
        this.assortiScraper = assortiScraper;
        this.scraper = scraper;
        this.productService = productService;
    }
    public static final int THOUSAND_SECONDS = 20000000;
    @Scheduled(fixedDelay = THOUSAND_SECONDS)
    public void scheduleScraping() {
        List<Product> allProducts;
        try {
            Map<String, ScraperInterface> scraperMap = new HashMap<>();
            scraperMap.put("https://www.barbora.lt/darzoves-ir-vaisiai", barboraScraper);
            scraperMap.put("https://www.barbora.lt/pieno-gaminiai-ir-kiausiniai", barboraScraper);
            scraperMap.put("https://www.barbora.lt/duonos-gaminiai-ir-konditerija", barboraScraper);
            scraperMap.put("https://www.barbora.lt/mesa-zuvis-ir-kulinarija", barboraScraper);
            scraperMap.put("https://www.barbora.lt/bakaleja", barboraScraper);
            scraperMap.put("https://www.rimi.lt/e-parduotuve/lt/produktai/vaisiai-darzoves-ir-geles/c/SH-15", rimiScraper);
            scraperMap.put("https://www.rimi.lt/e-parduotuve/lt/produktai/pieno-produktai-kiausiniai-ir-suris/c/SH-11", rimiScraper);
            scraperMap.put("https://www.rimi.lt/e-parduotuve/lt/produktai/duonos-gaminiai-ir-konditerija/c/SH-3", rimiScraper);
            scraperMap.put("https://www.rimi.lt/e-parduotuve/lt/produktai/mesa-zuvys-ir-kulinarija/c/SH-9", rimiScraper);
            scraperMap.put("https://www.rimi.lt/e-parduotuve/lt/produktai/bakaleja/c/SH-2", rimiScraper);
            scraperMap.put("https://www.assorti.lt/katalogas/maistas/darzoves-ir-vaisiai/", assortiScraper);
            scraperMap.put("https://www.assorti.lt/katalogas/maistas/pieno-gaminiai-ir-kiausiniai/", assortiScraper);
            scraperMap.put("https://www.assorti.lt/katalogas/maistas/duonos-gaminiai-ir-konditerija-2/", assortiScraper);
            scraperMap.put("https://www.assorti.lt/katalogas/maistas/sviezia-mesa-ir-paukstiena/", assortiScraper);
            scraperMap.put("https://www.assorti.lt/katalogas/maistas/zuvis-ir-zuvies-gaminiai/", assortiScraper);
            scraperMap.put("https://www.assorti.lt/katalogas/maistas/bakaleja/", assortiScraper);
            for (Map.Entry<String, ScraperInterface> entry : scraperMap.entrySet()) {
                String url = entry.getKey();
                ScraperInterface selectedScraper = entry.getValue();
                allProducts = scraper.scrapeProducts(url, selectedScraper);
                updateDatabase(allProducts);
            }
            }catch (Exception e) {
        }
    }
    public void updateDatabase(List<Product> allProducts) {
        try {
            if (allProducts != null && !allProducts.isEmpty()) {
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
                            existingProduct.setProductUrl(product.getProductUrl());
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

                // Update existing products in a single batch
                if (!productsToUpdate.isEmpty()) {
                    for (Product existingProduct : productsToUpdate){
                    productService.updateExistingProduct(existingProduct);
                    }
                }

                // Persist new products in a single batch
                if (!productsToAdd.isEmpty()) {
                    productService.persistProduct(productsToAdd);
                }
            }
            System.out.println("done");
        } catch (final Exception e) {
            LOGGER.error("Error occurred while updating the database", e);
        }
    }
}