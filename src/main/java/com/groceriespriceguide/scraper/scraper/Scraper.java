package com.groceriespriceguide.scraper.scraper;
import com.groceriespriceguide.products.entity.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        try{
            List<String> urlLinks = new ArrayList<>();
            List<Product> productCompleteList = new ArrayList<>();
            urlLinks.add("https://www.rimi.lt/e-parduotuve/lt/produktai/vaisiai-darzoves-ir-geles/c/SH-15");
            urlLinks.add("https://www.barbora.lt/darzoves-ir-vaisiai");
            urlLinks.add("https://www.assorti.lt/katalogas/maistas/darzoves-ir-vaisiai/");
            //urlLinks.add("https://lastmile.lt/chain/category/IKI/Vaisiai-ir-darzoves");
            try{
            for(String url:urlLinks)
            {
                List<Product> tempString = scrapeTheLinks(url);
                if (!tempString.isEmpty())productCompleteList.addAll(tempString);
            }
            return productCompleteList;}
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private List<Product> scrapeTheLinks(String url) throws Exception{
        try{
            List<Product> productsList = new ArrayList<>();
            String html = setUpHTML(url);
            Document doc = Jsoup.parse(html);
            int pages = getPages(doc,url);
            if (url.contains("barbora")){
                productsList = barboraScraper.parseBarbora(doc, url);
            } else if (url.contains("rimi")) {
                productsList = rimiScraper.parseRimi(doc,url);
            }
            else if (url.contains("IKI")) {
                productsList = ikiScraper.parseIKI(doc,url);
            }
            else if (url.contains("assorti")) {
                productsList = assortiScraper.parseIKI(doc,url);
            }
            if (pages > 0) productsList.addAll(loopThroughPages(doc,url,pages));
            return productsList;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String setUpHTML(String url) {
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

    private List<Product> loopThroughPages(Document doc, String url, int pages) {
        List<Product> pageProductList = new ArrayList<>();
        for (int y = 2; y< pages+1; y++){
            if (url.contains("barbora")){
                pageProductList.addAll(barboraScraper.parseBarbora(doc, url + "?page="+y));
            } else if (url.contains("rimi")) {
                pageProductList.addAll(rimiScraper.parseRimi(doc,url+"?page="+y));
            }
            else if (url.contains("IKI")) {
                pageProductList.addAll(ikiScraper.parseIKI(doc,url+"?page="+y));
            }
            else if (url.contains("assorti")) {
                pageProductList.addAll(assortiScraper.parseIKI(doc,url+"?page="+y));
            }
        }
        return pageProductList;
    }



    private Integer getPages(Document doc, String url) {
        try{
            int pageURL = 0;
            Elements categories = null;
            if (url.contains("barbora")||url.contains("assorti")){categories = doc.select("ul.pagination");}
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

    private int parsePagination(Elements pages, int pageURL) {
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