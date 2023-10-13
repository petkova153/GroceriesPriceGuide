package com.groceriespriceguide.scraper.scraper;

import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssortiScraper {
    @Autowired
    ScraperController scraperController;
    List<Product> parseAssorti(Page page) {
        List<Product> productList = new ArrayList<>();
        final String url = page.url();
        //rimi
        String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
        List<ElementHandle> products  = page.querySelectorAll("div.product_element");
        for (ElementHandle productEntity : products)
        {
            Product product = parseProductAssorti(productEntity,shop,url);
            productList.add(product);
        }
        return productList;
    }


    private Product parseProductAssorti(ElementHandle productEntity, String shop, String url) {
        try{
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElement(productEntity, "span.product_name", ""));
        final String tempPrice = scraperController.priceCleaner(
                scraperController.extractElement(productEntity, "span.main_price", ""));
        product.setProductPrice(Double.parseDouble(tempPrice.replace(",",".")));
        product.setProductUrl(scraperController.extractElement(productEntity, "a", "href"));
        product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
        product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        return product;}
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
