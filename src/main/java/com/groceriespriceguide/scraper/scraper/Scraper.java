package com.groceriespriceguide.scraper.scraper;
import com.groceriespriceguide.products.entity.Product;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Scraper {

    public List<Product> scrapeProducts() throws Exception {
        try{
            List<String> urlLinks = new ArrayList<>();
            List<Product> productCompleteList = new ArrayList<>();
            urlLinks.add("https://www.rimi.lt/e-parduotuve/lt/produktai/vaisiai-darzoves-ir-geles/c/SH-15");
            urlLinks.add("https://www.barbora.lt/darzoves-ir-vaisiai");
            for(String url:urlLinks)
            {
                productCompleteList =scrapeTheLinks(url);
            }
            return productCompleteList;
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
                productsList = parseBarbora(doc, url);
                return productsList;
            } else if (url.contains("rimi")) {
                productsList = parseRimi(doc,url);
                return productsList;
            }
            if (pages > 0) loopThroughPages(doc,url,pages);
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

    private void loopThroughPages(Document doc, String url, int pages) {
        for (int y = 2; y< pages+1; y++){
            System.out.println(y);
            if (url.contains("barbora")){
                parseBarbora(doc, url + "?page="+y);
            } else if (url.contains("rimi")) {
                parseRimi(doc,url+"?page="+y);
            }
        }
    }

    private List<Product> parseRimi(Document doc, String url) {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        //rimi
        String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
        Elements products  = doc.select("li.product-grid__item");
        for (Element productEntity : products)
        {
            product.setStore(shop);
            product.setProductName(extractElWithParser(productEntity, "p.card__name", "e\">", "</"));
            product.setProductPrice(extractElWithParser(productEntity, "p.card__price-per", "r\">", "</"));
            product.setProductUrl(shop + extractElement(productEntity, "a.card__url", "href"));
            product.setProductCategory(categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
            productList.add(product);
        }
        return productList;
    }

    private String categoryTranslator(String attr) {
        Map<String, String> catLT_EN = new HashMap<>();
        catLT_EN.put("darzoves", "Fruits and Vegetables");
        catLT_EN.put("pieno", "Dairy and eggs");
        catLT_EN.put("duonos", "Bakery");
        catLT_EN.put("mesa", "Meat,fish, and ready meals");
        catLT_EN.put("bakaleja", "Pantry staples");
        for (Map.Entry<String,String> category: catLT_EN.entrySet()){
            if (attr.contains(category.getKey())){
                return category.getValue();
            }
        }
        return null;
    }

    private List<Product> parseBarbora(Document doc, String url) {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
            //barbora
            Elements products = doc.select("div.b-product--wrap2");
            String shop = url.substring(url.indexOf("www."), url.indexOf(".lt") + 3);

            for (Element productEntity : products) {
                product.setStore(shop);
                product.setProductName(extractElement(productEntity, "img", "alt"));
                product.setProductPrice(extractElement(productEntity, "span.b-product-price-current-number", "content"));
                product.setProductUrl(shop + extractElement(productEntity, "a.b-product--imagelink", "href"));
                product.setProductCategory(categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
                productList.add(product);
            }
            return productList;
    }

    private String extractElement(Element product, String spanEl, String element) {
        Elements spans = product.select(spanEl);
        for (Element span : spans) {
            return span.attr(element);
        }
        return null;
    }

    private String extractElWithParser(Element product, String spanEl, String string1ToIndex, String string2ToIndex) {
        Elements spans = product.select(spanEl);
        for (Element span : spans) {
            String elementToParse = span.toString();
            return elementToParse.substring(elementToParse.indexOf(string1ToIndex)+3,elementToParse.indexOf(string2ToIndex));
        }
        return null;
    }

    private Integer getPages(Document doc, String url) {
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