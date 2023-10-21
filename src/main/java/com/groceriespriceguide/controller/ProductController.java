package com.groceriespriceguide.controller;

import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.repository.ProductRepository;
import com.groceriespriceguide.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController {
    final ProductService productService;
    final ProductRepository productRepository;
    public ProductController(final ProductService productService, final ProductRepository productRepository){
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public String listAndFilterProducts(
            @RequestParam(value = "selectedStores", required = false) List<String> selectedStores,
            @RequestParam(value = "selectedCategories", required = false) List<String> selectedCategories,
            @RequestParam(value = "query", required = false) String selectedName,
            Model model,
            @CookieValue(value = "loggedInUserId", defaultValue = "") String userId
    ) {
        List<String> availableStores = getAvailableStores();
        List<String> availableCategories = getAvailableCategories();
        model.addAttribute("availableStores", availableStores);
        model.addAttribute("availableCategories", availableCategories);

        if (userId.isEmpty()) {
            model.addAttribute("userLogged", "not logged");
        } else {
            model.addAttribute("userLogged", "logged");
        }

        List<Product> products;

        if (selectedStores != null || selectedCategories != null || selectedName != null) {
            // Filter products based on user input
            products = productService.searchProducts(selectedName, selectedStores, selectedCategories);
        } else {
            // List all products
            products = productRepository.findAll();
        }

        model.addAttribute("products", products);
        return "products"; // Thymeleaf view name
    }


    ////Searching and Filtering good stuff////
    public static List<String> getAvailableStores() {
        return Arrays.asList("rimi", "barbora", "assorti");
    }

    public static List<String> getAvailableCategories() {
        return Arrays.asList("Fruits and Vegetables", "Dairy and eggs", "Bakery","Meat,fish, and ready meals", "Pantry staples");
    }

    //Sorting
    @GetMapping("/sort")
    public String sortProducts(@RequestParam(name = "sortBy", defaultValue = "name_asc") String sortBy, Model model) {
        List<Product> products = productService.getSortedProducts(sortBy);
        model.addAttribute("products", products);
        model.addAttribute("sortBy", sortBy); // Add sortBy to the model to pre-select the dropdown in the view
        return "products";
    }


}
