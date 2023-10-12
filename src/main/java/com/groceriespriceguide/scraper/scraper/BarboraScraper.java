package com.groceriespriceguide.scraper.scraper;

import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class BarboraScraper {
    @Autowired
    ScraperController scraperController;
    List<Product> parseBarbora(Page doc) {
        List<Product> productList = new ArrayList<>();
        final String url = doc.url();
        //barbora
        List<ElementHandle> products = doc.querySelectorAll("li");
        System.out.println("here");
        System.out.println(products);
        String shop = url.substring(url.indexOf("www."), url.indexOf(".lt") + 3);
        for (ElementHandle productEntity : products)
        {
            Product product = parseProductBarbora(productEntity,shop,url);

            productList.add(product);
        }
        return productList;
    }



    private Product parseProductBarbora(ElementHandle productEntity, String shop, String url) {
        try{
        Product product = new Product();
        product.setStore(shop);
            System.out.println(productEntity.toString());
        product.setProductName(scraperController.extractElement(productEntity, "img", "alt"));
        String tempValuePrice = scraperController.extractElement(productEntity, "span.tw-text-b-paragraph-xs", "");
        product.setProductPrice(Double.parseDouble(tempValuePrice.replace(",",".")));
        product.setProductUrl(shop + scraperController.extractElement(productEntity, "a.b-product--imagelink", "href"));
        product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
        product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        return product;}
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }


}
