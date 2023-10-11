package com.groceriespriceguide.scraper.scraper;

import com.groceriespriceguide.entity.Product;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssortiScraper {
    @Autowired
    ScraperController scraperController;
    List<Product> parseIKI(Document doc, String url) {
        List<Product> productList = new ArrayList<>();
        //rimi
        String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
        Elements products  = doc.select("div.product_element");
        for (Element productEntity : products)
        {
            Product product = parseProductIKI(productEntity,shop,url);
            productList.add(product);
        }
        return productList;
    }


    private Product parseProductIKI(Element productEntity, String shop, String url) {
        try{
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElement(productEntity, "span.product_name", ""));
        String tempValuePrice = scraperController.priceCleaner(scraperController.extractElement(productEntity, "span.main_price", ""));
        if (!tempValuePrice.contains(",")){
            tempValuePrice = scraperController.extractElWithParser(productEntity, "span.main_price", "Akcija</span>", "<span> €");
        }
        product.setProductPrice(Double.parseDouble(tempValuePrice.replace(",",".")));
        product.setProductUrl(shop + scraperController.extractElement(productEntity, "a", "href"));
        product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
        product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        return product;}
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
