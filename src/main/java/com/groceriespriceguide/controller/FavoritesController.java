package com.groceriespriceguide.controller;


import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.repository.ProductRepository;
import com.groceriespriceguide.services.FavoriteService;
import com.groceriespriceguide.services.ProductService;
import com.groceriespriceguide.services.UserService;
import com.groceriespriceguide.users.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
//    @GetMapping("/user/{userId}")
//    public List<Favorites> getFavoriteProductsForUser(@RequestParam("userId") String userId) {
//        Long userIdLong = Long.parseLong(userId);
//        UserEntity user = userService.getUserById(userIdLong);
//        System.out.println(user);
//        return favoritesService.getFavoriteProductsForUser(user);
//    }

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
            Favorites favorite = favoriteService.addToFaves(user, product);
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
    @PostMapping("/remove")
    @ResponseBody
    public ApiResponse removeFromFavorites(@CookieValue(value = "loggedInUserId", defaultValue = "") String userId,
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
            Favorites favoriteToRemove = favoriteService.findByUserAndProduct(user,product);
            favoriteService.removeFromFavorites(favoriteToRemove);
            response.setStatus("favorite item successfully removed");
        } catch (Exception ex) {
            response.setStatus("error");
            response.setMessage(ex.getMessage());
        }
        return response;
    }
}
