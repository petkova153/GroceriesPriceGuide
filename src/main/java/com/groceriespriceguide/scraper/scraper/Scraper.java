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
    public List<Product> scrapeProducts(String url, ScraperInterface selectedScraper) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
            launchOptions.setHeadless(true);
            final List<Product> productCompleteList = new ArrayList<>();

            try {

                Browser browser = playwright.chromium().launch(launchOptions);
                Page page = browser.newPage();
                int timeoutMillis = 60000;
                page.navigate(url, new Page.NavigateOptions().setTimeout(timeoutMillis));
                page.waitForLoadState();


                List<Product> tempString = scrapeTheLinks(page, selectedScraper);
                assert tempString != null;
                if (!tempString.isEmpty()) {
                    productCompleteList.addAll(tempString);
                }
                browser.close();

                if (!productCompleteList.isEmpty()) return productCompleteList;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }


    private List<Product> scrapeTheLinks(Page page, ScraperInterface selectedScraper) {
        try {
            List<Product> productsList = new ArrayList<>();
            int pages = scraperController.getPages(page);

            if (selectedScraper != null) {
                productsList = selectedScraper.parse(page);
            }

            if (pages > 0) {
                productsList.addAll(loopThroughPages(page, pages, selectedScraper));
            }
            return productsList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private List<Product> loopThroughPages(Page page, int pages, ScraperInterface selectedScraper) {
        final List<Product> pageProductList = new ArrayList<>();

        for (int y = 2; y <= pages; y++) {
            String newPageUrl = page.url() + "?page=" + y;
            BrowserContext newContext = page.context().browser().newContext();
            Page newPage = newContext.newPage();
            newPage.navigate(newPageUrl);

            if (selectedScraper != null) {
                pageProductList.addAll(selectedScraper.parse(newPage));
            }

            // Navigate to the next page
            newContext.close();
        }
        return pageProductList;
    }
}