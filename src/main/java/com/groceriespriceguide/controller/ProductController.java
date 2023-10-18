package com.groceriespriceguide.controller;

import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.Search;
import com.groceriespriceguide.repository.ProductRepository;
import com.groceriespriceguide.services.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String listProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products"; // Thymeleaf view name
    }

    ////Searching and Filtering good stuff////

    @GetMapping("/search")
    public String showSearchPage(Model model) {
        // Populate stores and categories for checkbox options
        List<String> availableStores = Arrays.asList("Rimi", "Assorti", "Barbora");
        List<String> availableCategories = Arrays.asList("Fruits and Vegetables", "Dairy and eggs", "Bakery", "Meat, fish, and ready meals", "Pantry staples");

        model.addAttribute("availableStores", availableStores);
        model.addAttribute("availableCategories", availableCategories);
        model.addAttribute("searchForm", new Search());

        return "search";

    }

    @PostMapping("/search")
    public String searchProducts(@ModelAttribute Search searchForm, Model model) {
        String keyword = searchForm.getKeyword();
        List<String> selectedStores = searchForm.getSelectedStores();
        List<String> selectedCategories = searchForm.getSelectedCategories();

        List<Product> searchResults = productService.searchProducts(keyword, selectedStores, selectedCategories);
        model.addAttribute("searchResults", searchResults);

        // Populate stores and categories for checkbox options
        List<String> availableStores = Arrays.asList("Rimi", "Assorti", "Barbora");
        List<String> availableCategories = Arrays.asList("Fruits and Vegetables", "Dairy and eggs", "Bakery", "Meat, fish, and ready meals", "Pantry staples");

        model.addAttribute("availableStores", availableStores);
        model.addAttribute("availableCategories", availableCategories);
        model.addAttribute("searchForm", searchForm);

        return "search";
    }




}
