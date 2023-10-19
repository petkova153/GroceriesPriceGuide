package com.groceriespriceguide.controller;


import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.repository.FavoritesRepository;
import com.groceriespriceguide.services.FavoriteService;
import com.groceriespriceguide.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {
    // Endpoint to get favorite products for a user
    @Autowired
    private FavoriteService favoritesService;
    @Autowired
    private UserService userService;
    @GetMapping("/user/{userId}")
    public List<Favorites> getFavoriteProductsForUser(@RequestParam("userId") String userId) {
        Long userIdLong = Long.parseLong(userId);
        UserEntity user = userService.getUserById(userIdLong);
        System.out.println(user);
        return favoritesService.getFavoriteProductsForUser(user);
    }

    // Endpoint to add a product to favorites
    @PostMapping("/add")
    public ResponseEntity<String> addToFavorites(@RequestParam("productId") Long productId, @RequestParam("userId") String userId) {
        try {
            Long userIdLong = Long.parseLong(userId);
            favoritesService.addToFaves(productId, userIdLong);
            return ResponseEntity.ok("Product added to favorites.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Endpoint to remove a product from favorites
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestBody Favorites favoriteToRemove) {
        try {
            favoritesService.removeFromFavorites(favoriteToRemove);
            return ResponseEntity.ok("Product removed from favorites.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to delete all favorites for a user
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteAllFavoritesForUser(@PathVariable String userId) {
        Long userIdLong = Long.parseLong(userId);
        UserEntity user = userService.getUserById(userIdLong);
        return ResponseEntity.ok("All favorites deleted for the user.");
    }

}
