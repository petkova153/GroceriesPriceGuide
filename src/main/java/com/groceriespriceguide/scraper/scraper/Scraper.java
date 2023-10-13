package com.groceriespriceguide.scraper.scraper;
import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Transactional
@Component
public class Scraper {
    @Autowired
    BarboraScraper barboraScraper;
    @Autowired
    RimiScraper rimiScraper;
    @Autowired
    IkiScraper ikiScraper;
    @Autowired
    AssortiScraper assortiScraper;
    @Transactional
    public List<Product> scrapeProducts() throws Exception {
        try (Playwright playwright = Playwright.create())
        {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
            final List<String> urlLinks = new ArrayList<>();
            final List<Product> productCompleteList = new ArrayList<>();
            //urlLinks.add("https://www.rimi.lt/e-parduotuve/lt/produktai/vaisiai-darzoves-ir-geles/c/SH-15");
            urlLinks.add("https://www.barbora.lt/darzoves-ir-vaisiai");
            //urlLinks.add("https://www.assorti.lt/katalogas/maistas/darzoves-ir-vaisiai/");
            //urlLinks.add("https://lastmile.lt/chain/category/IKI/Darzoves");

            for(String url:urlLinks)
            {
                Browser browser = playwright.chromium().launch(launchOptions);
                Page page = browser.newPage();
                page.navigate(url);
                page.waitForLoadState();
                try{
                List<Product> tempString = scrapeTheLinks(page);
                if (!tempString.isEmpty()) {
                    productCompleteList.addAll(tempString);
                }
                browser.close();
                return productCompleteList;
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            }
            return null;
    }

    private List<Product> scrapeTheLinks(Page page) throws Exception{
        try{
            List<Product> productsList = new ArrayList<>();
            int pages = getPages(page);
            String url = page.url();
            if (url.contains("barbora")){
                productsList = barboraScraper.parseBarbora(page);
            } else if (url.contains("rimi")) {
                productsList = rimiScraper.parseRimi(page);
            }
            else if (url.contains("IKI")) {
                productsList = ikiScraper.parseIKI(page);
            }
            else if (url.contains("assorti")) {
                productsList = assortiScraper.parseAssorti(page);
            }
            if (pages > 0) productsList.addAll(loopThroughPages(page,pages));
            return productsList;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private List<Product> loopThroughPages(Page page, int pages) {
        final List<Product> pageProductList = new ArrayList<>();
        for (int y = 2; y <= pages; y++) {
            String newPageUrl = page.url() + "?page=" + y;
            BrowserContext newContext = page.context().browser().newContext();
            Page newPage = newContext.newPage();
            newPage.navigate(newPageUrl);
            if (newPageUrl.contains("barbora")) {
                pageProductList.addAll(barboraScraper.parseBarbora(newPage));
            } else if (page.url().contains("rimi")) {
                pageProductList.addAll(rimiScraper.parseRimi(newPage));
            } else if (page.url().contains("iki")) {
                System.out.println("here");
                pageProductList.addAll(ikiScraper.parseIKI(newPage));
            } else if (page.url().contains("assorti")) {
                pageProductList.addAll(assortiScraper.parseAssorti(newPage));
            }

            // Navigate to the next page
            newContext.close();
        }
        return pageProductList;
    }

    private Integer getPages(Page page) {
        try{
            int pageURL = 0;
            ElementHandle categories = null;
            String url = page.url();
            if (url.contains("barbora")||url.contains("assorti")){
                categories = page.querySelector("ul.pagination");
            }else if(url.contains("rimi")){
                categories = page.querySelector("ul.pagination__list");
            }
            if (categories != null){
                List<ElementHandle> pages = categories.querySelectorAll("a");
                pageURL = parsePagination(pages, pageURL);
            }
            return pageURL;
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private int parsePagination(List<ElementHandle> pages, int pageURL) {
        for (ElementHandle page : pages) {
            String pageNumbers = page.innerText();
            if (pageNumbers.length() < 3) {
                try {
                    int pageNumber = Integer.parseInt(pageNumbers);
                    if (pageNumber > pageURL) {
                        pageURL = pageNumber;
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where parsing to an integer fails (e.g., non-integer text)
                    System.err.println("Skipping non-integer: " + pageNumbers);
                }
            }
        }
        return pageURL;
    }
}