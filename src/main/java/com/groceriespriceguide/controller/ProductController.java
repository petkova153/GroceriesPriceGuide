package com.groceriespriceguide.controller;

import com.groceriespriceguide.services.ProductService;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {
    final ProductService productService;
    public ProductController(final ProductService productService){
        this.productService = productService;
    }
}
