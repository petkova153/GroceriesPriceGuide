package com.groceriespriceguide.controller;


import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.repository.FavoritesRepository;
import com.groceriespriceguide.services.FavoriteService;
import com.groceriespriceguide.services.ProductService;
import com.groceriespriceguide.services.UserService;
import com.groceriespriceguide.users.ApiResponse;
import com.groceriespriceguide.users.FavoriteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/favorites")
public class FavoritesController {
    // Endpoint to get favorite products for a user
    @Autowired
    private FavoriteService favoritesService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @GetMapping("/user/{userId}")
    public List<Favorites> getFavoriteProductsForUser(@RequestParam("userId") String userId) {
        Long userIdLong = Long.parseLong(userId);
        UserEntity user = userService.getUserById(userIdLong);
        System.out.println(user);
        return favoritesService.getFavoriteProductsForUser(user);
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiResponse addToFavorites(@CookieValue(value = "loggedInUserId", defaultValue = "") String userId,
                                      @RequestParam(value = "productID") Long productID) {
        System.out.println("product"+productID);
        System.out.println("user"+userId);
        ApiResponse response = new ApiResponse();
        try {

            Long currentUserId = Long.parseLong(userId);
            if (currentUserId == null){throw new NotFoundException("User not found with ID: " + userId);}


            // Add the product to favorites here
            UserEntity user = userService.getUserById(currentUserId);
            Product product = productService.findProductById(productID);
            System.out.println(user);
            System.out.println(product);
            Favorites favorite = favoritesService.addToFaves(user, product);
            System.out.println(favorite);
            response.setStatus("success");
        } catch (Exception ex) {
            response.setStatus("error");
            response.setMessage(ex.getMessage());
        }
        return response;
    }
}
