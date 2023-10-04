package com.groceriespriceguide.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class scraper {
    public void scrapeProducts() throws InterruptedException {
        try{
            String url = "https://www.barbora.lt/darzoves-ir-vaisiai/darzoves-ir-grybai";
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
                response.append(inputLine);}
            in.close();
            String html = response.toString();
            Document doc = Jsoup.parse(html);

            //barbora
            Elements products  = doc.select("div.b-product--wrap2");
            String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
            Elements categories = doc.select("ul.pagination");
            for (Element category: categories){
                Elements pages = category.select("a");
                for (Element page: pages){
                    String links = page.attr("href");
                    System.out.println(links);
                }
                break;
            }

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

    //iki
    //			Elements products  = doc.select("div.css-jpvoog");
    //			System.out.println(doc);
    //			for (Element product : products){
    //
    //				Elements spans = product.select("p.css-abaudk");
    //				for (Element link : spans) {
    //					System.out.println(link);
    //				}
    //
    //				Elements prices = product.select("p.css-lrnbrk");
    //				for (Element price : prices) {
    //					System.out.println(price);
    //				}
    //			}
    ////rimi
    //			String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
    //			Elements products  = doc.select("li.product-grid__item");
    //			for (Element product : products){
    //				System.out.println("Shop: " + shop);
    //				Elements spans = product.select("p.card__name");
    //				for (Element link : spans) {
    //					String pName = link.toString();
    //					String productP = pName.substring(pName.indexOf("e\">")+3,pName.indexOf("</"));
    //					System.out.println("Product: " + productP);
    //				}
    //				Elements prices = product.select("p.card__price-per");
    //				for (Element price : prices) {
    //					String pPrice = price.toString();
    //					String cleanPrice = pPrice.substring(pPrice.indexOf("r\">")+3,pPrice.indexOf("</"));
    //					System.out.println("Price: " + cleanPrice);
    //				}
    //				Elements hrefs = product.select("a.card__url");
    //				for (Element href : hrefs) {
    //					String uRLS = href.attr("href");
    //					System.out.println("URL: " + shop+uRLS);
    //				}
    //				Elements categories = doc.select("main");
    //				for (Element category: categories){
    //					System.out.println("Category: " + category.attr("data-gtms-content-category"));
    //				}
    //			}
        }
            catch(Exception e) {
                e.getMessage();
        }
    }
}
