package com.groceriespriceguide.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class Scraper {
    public void scrapeProducts() throws InterruptedException {
        //SpringApplication.run(WebScrapingApplication.class, args);
        try{
            String url = "https://www.rimi.lt/e-parduotuve/lt/produktai/vaisiai-darzoves-ir-geles/c/SH-15";

            String html = setUpHTML(url);
            Document doc = Jsoup.parse(html);
            int pages = getPages(doc);
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
            e.getMessage();
        }
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
            Elements spans = product.select("p.card__name");
            for (Element link : spans) {
                String pName = link.toString();
                String productP = pName.substring(pName.indexOf("e\">")+3,pName.indexOf("</"));
                System.out.println("Product: " + productP);
            }
            Elements prices = product.select("p.card__price-per");
            for (Element price : prices) {
                String pPrice = price.toString();
                String cleanPrice = pPrice.substring(pPrice.indexOf("r\">")+3,pPrice.indexOf("</"));
                System.out.println("Price: " + cleanPrice);
            }
            Elements hrefs = product.select("a.card__url");
            for (Element href : hrefs) {
                String uRLS = href.attr("href");
                System.out.println("URL: " + shop+uRLS);
            }
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
            Elements spans = product.select("img");
            for (Element link : spans) {
                String productName = link.attr("alt");
                System.out.println("Product: " + productName);
            }

            Elements prices = product.select("span.b-product-price-current-number");
            for (Element price : prices) {
                String prc = price.attr("content");
                System.out.println("Price: " + prc);
            }
            Elements hrefs = product.select("a.b-product--imagelink");
            for (Element href : hrefs) {
                String uRLS = href.attr("href");
                System.out.println("URL: " + shop+uRLS);
            }
            System.out.println("Category: " + url.substring(url.indexOf(".lt/")+3));
        }
    }

    private static Integer getPages(Document doc) {
        try{
            int pageURL = 0;
            Elements categories = doc.select("ul.pagination");
            int numberPages = categories.size();
            if (numberPages == 0) categories = doc.select("ul.pagination__list");
            for (Element category: categories){
                Elements pages = category.select("a");
                for (Element page: pages){
                    String pageNumbers = page.toString();
                    pageNumbers = pageNumbers.substring(pageNumbers.indexOf("?page=")+6,pageNumbers.indexOf("\">"));
                    if (pageNumbers.length()<3) {
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
                break;
            }
            return pageURL;
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return null;
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
}