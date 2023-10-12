package com.groceriespriceguide.scraper.scraper;

import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class IkiScraper {
    @Autowired
    ScraperController scraperController;
    List<Product> parseIKI(Page doc) {
        List<Product> productList = new ArrayList<>();
        final String url = doc.url();
        String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
        List<ElementHandle> products  = doc.querySelectorAll("div.css-175oi2r");
        System.out.println(products);
        for (ElementHandle productEntity : products)
        {
            System.out.println(productEntity.querySelectorAll("li.product-grid__item"));
            Product product = parseProductIKI(productEntity,shop,url);
            productList.add(product);
        }
        return productList;
    }


    private Product parseProductIKI(ElementHandle productEntity, String shop, String url) {
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElement(productEntity, "img", "alt"));
        System.out.println(product.getProductName());
        String tempValuePrice = scraperController.priceCleaner(scraperController.extractElement(productEntity, "span.main_price", ""));
        System.out.println(tempValuePrice);
        product.setProductPrice(Double.parseDouble(tempValuePrice.replace(",",".")));
        product.setProductUrl(shop + scraperController.extractElement(productEntity, "a", "href"));
        product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
        product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        return product;
    }
}
