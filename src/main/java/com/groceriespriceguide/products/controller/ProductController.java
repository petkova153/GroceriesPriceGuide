package com.groceriespriceguide.products.controller;

import com.groceriespriceguide.products.services.ProductService;
import com.groceriespriceguide.scraper.scraper.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {
    final ProductService productService;
    public ProductController(final ProductService productService){
        this.productService = productService;
    }
}
