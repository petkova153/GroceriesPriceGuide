package com.groceriespriceguide.scraper.scraper;
import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Transactional
@Component
public class Scraper {


    final ScraperController scraperController;

    public Scraper(
                   final ScraperController scraperController) {
        this.scraperController = scraperController;
    }

    @Transactional
    public List<Product> scrapeALink(Page page, ScraperInterface selectedScraper) {
        try {
            List<Product> productsList = new ArrayList<>();
            if (selectedScraper != null) {
                productsList = selectedScraper.parse(page);
            }
            return productsList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}