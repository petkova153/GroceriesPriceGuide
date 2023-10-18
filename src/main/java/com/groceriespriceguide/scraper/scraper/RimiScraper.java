package com.groceriespriceguide.scraper.scraper;

import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class RimiScraper {
    @Autowired
    ScraperController scraperController;
    Scraper scraper;
    List<Product> parseRimi(Page doc) {
        List<Product> productList = new ArrayList<>();
        final String url = doc.url();
        //rimi
        String shop = url.substring(url.indexOf("www.")+4,url.indexOf(".lt"));
        List<ElementHandle> products  = doc.querySelectorAll("li.product-grid__item");
        for (ElementHandle productEntity : products)
        {
            Product product = parseProductRimi(productEntity,shop,url);
            if (product != null) productList.add(product);
        }
        return productList;
    }
    private Product parseProductRimi(ElementHandle productEntity, String shop, String url) {
        try{
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElWithParser(productEntity, "p.card__name", "e\">", "</"));
        String tempValuePrice = scraperController.extractElWithParser(productEntity, "div.price-tag", "an>", "</") +
                "." + scraperController.extractElWithParser(productEntity, "sup", "up>", "</");
        if (!tempValuePrice.contains("null")) product.setProductPrice(Double.parseDouble(tempValuePrice.replace(",",".")));
        product.setProductUrl(shop + scraperController.extractElement(productEntity, "a.card__url", "href"));
        product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
        product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        return product;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
