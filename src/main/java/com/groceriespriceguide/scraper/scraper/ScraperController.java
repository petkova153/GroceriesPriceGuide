package com.groceriespriceguide.scraper.scraper;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScraperController {

    String extractElement(Element product, String spanEl, String element) {
        Elements spans = product.select(spanEl);
        for (Element span : spans) {
            if (element.isEmpty()){
                String elementToParse = span.toString();
                elementToParse = elementToParse.strip();
                return elementToParse.substring(elementToParse.indexOf("e\">")+3,elementToParse.indexOf("</"));
            }
                else{
            return span.attr(element);}
        }
        return null;
    }

    String extractElWithParser(Element product, String spanEl, String string1ToIndex, String string2ToIndex) {
        Elements spans = product.select(spanEl);
        for (Element span : spans) {
            String elementToParse = span.toString();
            return elementToParse.substring(elementToParse.indexOf(string1ToIndex)+3,elementToParse.indexOf(string2ToIndex));
        }
        return null;
    }

    String categoryTranslator(String attr) {
        Map<String, String> catLT_EN = new HashMap<>();
        catLT_EN.put("darzoves", "Fruits and Vegetables");
        catLT_EN.put("pieno", "Dairy and eggs");
        catLT_EN.put("duonos", "Bakery");
        catLT_EN.put("mesa", "Meat,fish, and ready meals");
        catLT_EN.put("bakaleja", "Pantry staples");
        for (Map.Entry<String,String> category: catLT_EN.entrySet()){
            if (attr.contains(category.getKey())){
                return category.getValue();
            }
        }
        return null;
    }

    public String priceCleaner(String priceString) {
        try {
            String subPriceString;
            if (priceString.contains(",")) {
                subPriceString = priceString.substring(0, priceString.indexOf(",") + 3);
            } else {
                subPriceString = priceString.substring(0, priceString.indexOf(".") + 3);
            }
            return subPriceString;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
