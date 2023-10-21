package com.groceriespriceguide.scraper.scraper;
import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Transactional
@Component
public class Scraper {

    final BarboraScraper barboraScraper;
    final RimiScraper rimiScraper;
    final IkiScraper ikiScraper;
    final AssortiScraper assortiScraper;
    final ScraperController scraperController;

    public Scraper(final BarboraScraper barboraScraper, final RimiScraper rimiScraper,
                   final IkiScraper ikiScraper, final AssortiScraper assortiScraper,
                   final ScraperController scraperController) {
        this.barboraScraper = barboraScraper;
        this.rimiScraper = rimiScraper;
        this.ikiScraper = ikiScraper;
        this.assortiScraper = assortiScraper;
        this.scraperController = scraperController;
    }

    @Transactional
    public List<Product> scrapeProducts() {
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
            launchOptions.setHeadless(true);
            final List<Product> productCompleteList = new ArrayList<>();
            Map<String, ScraperInterface> scraperMap = new HashMap<>();
            scraperMap.put("https://www.barbora.lt/darzoves-ir-vaisiai", barboraScraper);
            scraperMap.put("https://www.rimi.lt/e-parduotuve/lt/produktai/vaisiai-darzoves-ir-geles/c/SH-15", rimiScraper);
            scraperMap.put("https://www.assorti.lt/katalogas/maistas/darzoves-ir-vaisiai/", assortiScraper);
            try {
            for (Map.Entry<String, ScraperInterface> entry : scraperMap.entrySet()) {
                String url = entry.getKey();
                ScraperInterface selectedScraper = entry.getValue();
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
            }
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