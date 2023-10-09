package com.groceriespriceguide.products;

import com.groceriespriceguide.scraper.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {
    @Autowired
    Scraper scraper;
}
