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
public class BarboraScraper {
    @Autowired
    ScraperController scraperController;
    List<Product> parseBarbora(Document doc, String url) {
        List<Product> productList = new ArrayList<>();
        //barbora
        Elements products = doc.select("li");
        System.out.println(doc);
        String shop = url.substring(url.indexOf("www."), url.indexOf(".lt") + 3);
        for (Element productEntity : products)
        {
            Product product = parseProductBarbora(productEntity,shop,url);

            productList.add(product);
        }
        return productList;
    }



    private Product parseProductBarbora(Element productEntity, String shop, String url) {
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
