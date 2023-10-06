package com.groceriespriceguide.scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class Scraper {
    public void scrapeProducts() throws InterruptedException {
        List<String> urlLinks = new ArrayList<>();
        urlLinks.add("https://www.rimi.lt/e-parduotuve/lt/produktai/vaisiai-darzoves-ir-geles/c/SH-15");
        urlLinks.add("https://www.barbora.lt/darzoves-ir-vaisiai");
        for(String url:urlLinks)
        {
            scrapeTheLinks(url);
        }

    }

    private static void scrapeTheLinks(String url) {
        try{
            String html = setUpHTML(url);
            Document doc = Jsoup.parse(html);
            int pages = getPages(doc,url);
            System.out.println(pages);
            if (url.contains("barbora")){
                parseBarbora(doc, url);
            } else if (url.contains("rimi")) {
                System.out.println("here");
                parseRimi(doc,url);
            }
            if (pages > 0) loopThroughPages(doc,url,pages);

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static String setUpHTML(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
// optional request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            System.out.println("Response code: " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void loopThroughPages(Document doc, String url, int pages) {
        for (int y = 2; y< pages+1; y++){
            System.out.println(y);
            if (url.contains("barbora")){
                parseBarbora(doc, url + "?page="+y);
            } else if (url.contains("rimi")) {
                parseRimi(doc,url+"?page="+y);
            }
        }
    }

    private static void parseRimi(Document doc, String url) {
        //rimi
        String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
        Elements products  = doc.select("li.product-grid__item");
        for (Element product : products){
            System.out.println("Shop: " + shop);
            System.out.println("Product: " + extractElWithParser(product,"p.card__name","e\">","</")) ;
            System.out.println("Price: " + extractElWithParser(product,"p.card__price-per","r\">","</"));
            System.out.println("URL: " + shop + extractElement(product,"a.card__url","href"));

            Elements categories = doc.select("main");
            for (Element category: categories){
                System.out.println("Category: " + category.attr("data-gtms-content-category"));
            }
        }
    }

    private static void parseBarbora(Document doc, String url) {
        //barbora
        Elements products  = doc.select("div.b-product--wrap2");
        String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);

        for (Element product : products){
            System.out.println("Shop: " + shop);
            System.out.println("Product: " + extractElement(product,"img","alt")) ;
            System.out.println("Price: " + extractElement(product,"span.b-product-price-current-number","content"));
            System.out.println("URL: " + shop + extractElement(product,"a.b-product--imagelink","href"));
            System.out.println("Category: " + url.substring(url.indexOf(".lt/")+3));
        }
    }

    private static String extractElement(Element product, String spanEl, String element) {
        Elements spans = product.select(spanEl);
        for (Element span : spans) {
            return span.attr(element);
        }
        return null;
    }

    private static String extractElWithParser(Element product, String spanEl, String string1ToIndex, String string2ToIndex) {
        Elements spans = product.select(spanEl);
        for (Element span : spans) {
            String elementToParse = span.toString();
            return elementToParse.substring(elementToParse.indexOf(string1ToIndex)+3,elementToParse.indexOf(string2ToIndex));
        }
        return null;
    }

    private static Integer getPages(Document doc, String url) {
        try{
            int pageURL = 0;
            Elements categories = null;
            if (url.contains("barbora")){categories = doc.select("ul.pagination");}
            else if(url.contains("rimi")){categories = doc.select("ul.pagination__list");}
            if (categories != null){
                for (Element category : categories) {
                    Elements pages = category.select("a");
                    pageURL = parsePagination(pages,pageURL);
                    break;
                }
            }
            return pageURL;
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static int parsePagination(Elements pages, int pageURL) {
        for (Element page : pages) {
            String pageNumbers = page.toString();
            pageNumbers = pageNumbers.substring(pageNumbers.indexOf("?page=") + 6, pageNumbers.indexOf("\">"));
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