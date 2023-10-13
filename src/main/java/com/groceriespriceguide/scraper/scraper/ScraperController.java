package com.groceriespriceguide.scraper.scraper;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.microsoft.playwright.ElementHandle;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScraperController {

    String extractElement(ElementHandle product, String spanEl, String element) {
        final List<ElementHandle> spans = product.querySelectorAll(spanEl);
        for (ElementHandle span : spans) {
            if (element.isEmpty()){
                String elementToParse = span.evaluate("el => el.outerHTML", "*").toString();
                elementToParse = elementToParse.strip();
                return elementToParse.substring(elementToParse.indexOf("e\">")+3,elementToParse.indexOf("</"));
            }
                else{
            return span.getAttribute(element);}
        }
        return null;
    }

    String extractTextContent(ElementHandle product, String spanEl) {
        final List<ElementHandle> spans = product.querySelectorAll(spanEl);
        for (ElementHandle span : spans) {
            return span.textContent();
        }
        return null;
    }

    String extractElWithParser(ElementHandle product, String spanEl, String string1ToIndex, String string2ToIndex) {
        final List<ElementHandle> spans = product.querySelectorAll(spanEl);
        for (ElementHandle span : spans) {
            String elementToParse = span.evaluate("el => el.outerHTML", "*").toString();
            return elementToParse.substring(elementToParse.indexOf(string1ToIndex)+string1ToIndex.length(),
                    elementToParse.indexOf(string2ToIndex));
        }
        return null;
    }

    String categoryTranslator(String attr) {
        final Map<String, String> catLT_EN = new HashMap<>();
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
            final String subPriceString;
            if (priceString.contains(",")) {
                subPriceString = priceString.substring(0, priceString.indexOf(",") + 3);
            } else {
                subPriceString = priceString.substring(0, priceString.indexOf(".") + 3);
            }
            if(subPriceString.contains("Akcija ")) return subPriceString.replace("Akcija ", "");
            return subPriceString;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    String extractNthElement(ElementHandle product, String spanEl, int nthEl) {
        final List<ElementHandle> spans = product.querySelectorAll(spanEl);
        int counter = 0;
        for (ElementHandle span : spans) {
            final List<ElementHandle> subSpans = span.querySelectorAll("*");
            for (ElementHandle e : subSpans)
            {
                if (counter <= nthEl) {
                    counter++;
                    System.out.println(e.evaluate("el => el.outerHTML", "*").toString());
                } else {
                    return e.evaluate("el => el.outerHTML", "*").toString();
                }
            }
        }
        return null;
    }

}
