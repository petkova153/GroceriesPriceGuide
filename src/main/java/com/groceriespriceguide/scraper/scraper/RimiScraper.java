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
public class RimiScraper {
    @Autowired
    ScraperController scraperController;
    Scraper scraper;
    List<Product> parseRimi(Document doc, String url) {
        List<Product> productList = new ArrayList<>();
        //rimi
        String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
        Elements products  = doc.select("li.product-grid__item");
        for (Element productEntity : products)
        {
            Product product = parseProductRimi(productEntity,shop,url);
            productList.add(product);
        }
        return productList;
    }
    private Product parseProductRimi(Element productEntity, String shop, String url) {
        try{
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElWithParser(productEntity, "p.card__name", "e\">", "</"));
        String tempValuePrice = scraperController.extractElWithParser(productEntity, "div.price-tag", "an>", "</") +
                "." + scraperController.extractElWithParser(productEntity, "sup", "up>", "</");

        product.setProductPrice(Double.parseDouble(tempValuePrice.replace(",",".")));
        product.setProductUrl(shop + scraperController.extractElement(productEntity, "a.card__url", "href"));
        product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
        product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        return product;}
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
