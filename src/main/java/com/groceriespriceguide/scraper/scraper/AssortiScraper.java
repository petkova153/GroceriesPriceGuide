package com.groceriespriceguide.scraper.scraper;

import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssortiScraper implements ScraperInterface {
    final ScraperController scraperController;
    public AssortiScraper(final ScraperController scraperController){
        this.scraperController = scraperController;
    }
    public List<Product> parse(Page page) {
        List<Product> productsList = new ArrayList<>();
        final String url = page.url();
        //rimi
        String shop = url.substring(url.indexOf("www.")+4,url.indexOf(".lt"));
        List<ElementHandle> products  = page.querySelectorAll("div.product_element");
        for (ElementHandle productEntity : products)
        {
            Product product = parseProductAssorti(productEntity,shop,url);
            if (product != null) productsList.add(product);
        }
        return productsList;
    }


    private Product parseProductAssorti(ElementHandle productEntity, String shop, String url) {
        try{
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElement(productEntity, "span.product_name", ""));
        final String tempPrice = scraperController.priceCleaner(
                scraperController.extractTextContent(productEntity, "span.main_price"));
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
