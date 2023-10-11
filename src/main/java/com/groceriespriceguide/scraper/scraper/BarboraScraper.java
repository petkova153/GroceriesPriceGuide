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
        Elements products = doc.select("div.b-product--wrap2");
        String shop = url.substring(url.indexOf("www."), url.indexOf(".lt") + 3);

        for (Element productEntity : products)
        {
            Product product = parseProductBarbora(productEntity,shop,url);
            productList.add(product);
        }
        return productList;
    }



    private Product parseProductBarbora(Element productEntity, String shop, String url) {
        Product product = new Product();
        product.setStore(shop);
        product.setProductName(scraperController.extractElement(productEntity, "img", "alt"));
        product.setProductPrice(scraperController.extractElement(productEntity, "span.b-product-price-current-number", "content"));
        product.setProductUrl(shop + scraperController.extractElement(productEntity, "a.b-product--imagelink", "href"));
        product.setPictureUrl(scraperController.extractElement(productEntity, "img", "src"));
        product.setProductCategory(scraperController.categoryTranslator(url.substring(url.indexOf(".lt/") + 3)));
        System.out.println(scraperController.extractElWithParser(productEntity, "div.b-product-price--extra", "v\">", "</"));
        return product;
    }


}
