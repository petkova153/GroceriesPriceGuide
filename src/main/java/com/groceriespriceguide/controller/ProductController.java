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
    public String listAndFilterAndSortProducts(
            @RequestParam(value = "selectedStores", required = false) List<String> selectedStores,
            @RequestParam(value = "selectedCategories", required = false) List<String> selectedCategories,
            @RequestParam(value = "query", required = false) String selectedName,
            @RequestParam(value = "sortBy", defaultValue = "name_asc") String sortBy,
            @RequestParam(value = "clearFilters", required = false) String clearFilters,
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

        // Initialize the products list to all products
        List<Product> products = productService.getProductData();

        // Log the input values for debugging
        System.out.println("selectedStores: " + selectedStores);
        System.out.println("selectedCategories: " + selectedCategories);
        System.out.println("selectedName: " + selectedName);

        // Filter products based on user input
        if (selectedCategories != null && !selectedCategories.isEmpty() || selectedStores != null && !selectedStores.isEmpty()
                || selectedName != null && !selectedName.isEmpty()) {
            products = productService.searchProducts(selectedName, selectedStores, selectedCategories);
        } else {
            System.out.println("No filtering applied. Returning all products.");
        }

        // Log the number of products after filtering
        System.out.println("Number of products after filtering: " + products.size());

        // Sort products based on the selected sorting option
        if (!sortBy.equals("unsorted")) {
            products = productService.getSortedProducts(products, sortBy);
        }

        if (clearFilters != null) {
            // Handle "Clear All Filters" button click
            // Reset all filter parameters to their default values
            selectedStores = null;
            selectedCategories = null;
            selectedName = "";
            sortBy = "name_asc";
            products = productService.getProductData();
        }

        model.addAttribute("products", products);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("selectedStores", selectedStores);
        model.addAttribute("selectedCategories", selectedCategories);
        model.addAttribute("selectedName", selectedName);
        return "products";
    }




    ////Searching and Filtering good stuff////
    public static List<String> getAvailableStores() {
        return Arrays.asList("rimi", "barbora", "assorti");
    }

    public static List<String> getAvailableCategories() {
        return Arrays.asList("Fruits and Vegetables", "Dairy and eggs", "Bakery","Meat fish and ready meals", "Pantry staples");
    }


}
