package com.groceriespriceguide.controller;


import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.services.FavoriteService;
import com.groceriespriceguide.services.ProductService;
import com.groceriespriceguide.services.UserService;
import com.groceriespriceguide.users.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/favorites")
public class FavoritesController {
    // Endpoint to get favorite products for a user

    final ProductService productService;
    final UserService userService;
    final FavoriteService favoriteService;
    public FavoritesController(final ProductService productService, final UserService userService, final FavoriteService favoriteService){
        this.productService = productService;
        this.userService = userService;
        this.favoriteService = favoriteService;
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiResponse addToFavorites(@CookieValue(value = "loggedInUserId", defaultValue = "") String userId,
                                      @RequestParam(value = "productID") Long productID) {
        ApiResponse response = new ApiResponse();
        try {

            Long currentUserId = Long.parseLong(userId);
            if (currentUserId == null){throw new NotFoundException("User not found with ID: " + userId);}


            // Add the product to favorites here
            UserEntity user = userService.getUserById(currentUserId);
            Product product = productService.findProductById(productID);
            favoriteService.addToFaves(user, product);
            response.setStatus("success");
        } catch (Exception ex) {
            response.setStatus("error");
            response.setMessage(ex.getMessage());
        }
        return response;
    }
    @GetMapping()
    public String showAllFavorites(@CookieValue(value = "loggedInUserId", defaultValue = "") String userId,
                                   Model model) {
                 Long currentUserId = Long.parseLong(userId);
            if (currentUserId == null){throw new NotFoundException("User not found with ID: " + userId);}


            // Add the product to favorites here
            UserEntity user = userService.getUserById(currentUserId);
            List<Favorites> favorites = favoriteService.getFavoriteProductsForUser(user);

            model.addAttribute("favorites", favorites);
        return "favorites";
    }
    @GetMapping("/remove")
    @ResponseBody
    public ApiResponse removeFromFavorites(@CookieValue(value = "loggedInUserId", defaultValue = "") String userId,
                                           @RequestParam(value = "productID") Long productID) {
        System.out.println("product" + productID);
        System.out.println("user" + userId);
        ApiResponse response = new ApiResponse();
        try {
            Long currentUserId = Long.parseLong(userId);
            if (currentUserId == null) {
                throw new NotFoundException("User not found with ID: " + userId);
            }

            UserEntity user = userService.getUserById(currentUserId);
            Product product = productService.findProductById(productID);

            if (user == null || product == null) {
                throw new NotFoundException("User or product not found.");
            }

            Favorites favoriteToRemove = favoriteService.findByUserAndProduct(user, product);
            if (favoriteToRemove != null) {
                favoriteService.removeFromFavorites(favoriteToRemove);
                response.setStatus("success");
            } else {
                throw new NotFoundException("Favorite not found for user and product.");
            }
        } catch (NotFoundException ex) {
            response.setStatus("error");
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setStatus("error");
            response.setMessage("Failed to remove item from favorites. Error: " + ex.getMessage());
        }
        System.out.println(response.getStatus());
        return response;
    }

}
