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
        List<ElementHandle> products = doc.querySelectorAll("li[data-testid]");
        String shop = url.substring(url.indexOf("www."), url.indexOf(".lt") + 3);
        for (ElementHandle productEntity : products)
        {
            Product product = parseProductBarbora(productEntity,shop,url);

            if (product != null) productList.add(product);
        }
        return productList;
    }



    private Product parseProductBarbora(ElementHandle productEntity, String shop, String url) {
        try{
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElement(productEntity, "img", "alt"));
            product.setProductUrl(shop + scraperController.extractElement(productEntity, "a", "href"));
            product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
            product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        String tempValuePrice = scraperController.extractElWithParser(productEntity, "//span[contains(@class, 'tw-mr-0.5')][contains(@class, 'tw-text-b-price-sm')][contains(@class, 'tw-font-semibold')][contains(@class, 'lg:tw-text-b-price-xl')]", ">","</") +
                "." + scraperController.extractElWithParser(productEntity, "//span[contains(@class, 'tw-text-b-price-xs')][contains(@class, 'tw-font-semibold')][contains(@class, 'lg:tw-text-b-price-lg')]", ">","</");
        if (!tempValuePrice.contains("null")) product.setProductPrice(Double.parseDouble(tempValuePrice));
        return product;}
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }


}
