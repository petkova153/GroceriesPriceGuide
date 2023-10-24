package com.groceriespriceguide.scraper.scheduler;

import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.scraper.scraper.*;
import com.groceriespriceguide.services.ProductService;
import com.microsoft.playwright.*;
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
    final ScraperController scraperController;

    public Scheduler(final Scraper scraper, final ProductService productService,final BarboraScraper barboraScraper, final RimiScraper rimiScraper,
                     final IkiScraper ikiScraper, final AssortiScraper assortiScraper, final ScraperController scraperController){
        this.barboraScraper = barboraScraper;
        this.rimiScraper = rimiScraper;
        this.ikiScraper = ikiScraper;
        this.assortiScraper = assortiScraper;
        this.scraper = scraper;
        this.productService = productService;
        this.scraperController = scraperController;
    }
    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduleScraping() {

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
                try (Playwright playwright = Playwright.create()) {
                    BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
                    launchOptions.setHeadless(true);
                    Browser browser = playwright.chromium().launch(launchOptions);
                    Page page = loadAPage(url,browser);
                    int pages = scraperController.getPages(page);
                    loopPages(pages,page,selectedScraper);
                    browser.close();
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loopPages(int pages, Page page,ScraperInterface selectedScraper){
        List<Product> allProducts;
        try {
            allProducts = scraper.scrapeALink(page, selectedScraper);
            System.out.println(page.url());
            updateDatabase(allProducts);
            for (int y = 2; y <= pages; y++) {
                List<Product> moreProducts;
                BrowserContext newContext = page.context().browser().newContext();
                Page newPage = newContext.newPage();
                String newPageUrl = page.url() + "?page=" + y;
                System.out.println("url: " + newPageUrl);
                newPage.navigate(newPageUrl);
                moreProducts = scraper.scrapeALink(newPage, selectedScraper);
                newContext.close();
                updateDatabase(moreProducts);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private Page loadAPage(String url, Browser browser){
            Page page = browser.newPage();
            int timeoutMillis = 60000;
            page.navigate(url, new Page.NavigateOptions().setTimeout(timeoutMillis));
            page.waitForLoadState();
            return page;
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
                            existingProduct.setProductCategory(product.getProductCategory());
                            if (existingProduct.getProductPrice() != null &&
                                    product.getProductPrice()!=null)existingProduct.setPriceChange(existingProduct.getProductPrice()-
                                    product.getProductPrice());
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
                    System.out.println(productsToAdd);
                    productService.persistProduct(productsToAdd);
                }
            }
            System.out.println("done");
        } catch (final Exception e) {
            LOGGER.error("Error occurred while updating the database", e);
        }
    }
}