package com.groceriespriceguide.scraper.scraper;

import com.groceriespriceguide.products.entity.Product;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class IkiScraper {
    @Autowired
    ScraperController scraperController;
    List<Product> parseIKI(Document doc, String url) {
        List<Product> productList = new ArrayList<>();
        //rimi
        String shop = url.substring(url.indexOf("www."),url.indexOf(".lt")+3);
        Elements products  = doc.select("div._css-175oi2r");
        for (Element productEntity : products)
        {
            Product product = parseProductIKI(productEntity,shop,url);
            productList.add(product);
        }
        return productList;
    }


    private Product parseProductIKI(Element productEntity, String shop, String url) {
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElement(productEntity, "img", "alt"));
        product.setProductPrice(scraperController.extractElement(productEntity, "span.b-product-price-current-number", "content"));
        product.setProductUrl(shop + scraperController.extractElement(productEntity, "a", "href"));
        product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
        product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        return product;
    }
}
