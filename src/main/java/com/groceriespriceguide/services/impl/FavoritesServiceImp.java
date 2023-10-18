package com.groceriespriceguide.services.impl;

import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.repository.FavoritesRepository;
import com.groceriespriceguide.repository.ProductRepository;
import com.groceriespriceguide.repository.UserRepository;
import com.groceriespriceguide.services.FavoriteService;
import com.groceriespriceguide.services.ProductService;
import com.groceriespriceguide.services.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoritesServiceImp implements FavoriteService {



    private final FavoritesRepository favoritesRepository;
    private final ProductService productService;
    private final UserService userService;

    public FavoritesServiceImp( final FavoritesRepository favoritesRepository, final ProductService productService, final UserService userService)
    {
       this.userService=userService;
        this.productService = productService;
        this.favoritesRepository= favoritesRepository;
    }

    public List<Favorites> getFavoriteProductsForUser(UserEntity user){
        return favoritesRepository.findByUser(user);
    }
    public List<Favorites> findByUserAndProduct(UserEntity user, Product product){
        return favoritesRepository.findByUserAndProduct(user, product);
    }


    public void addToFaves(Long productID, Long userID) throws Exception {
        Product product = productService.findProductById(productID);

        UserEntity user = userService.getUserById(userID);

        if (product != null && user != null) {
            List<Favorites> existingFavorites = favoritesRepository.findByUserAndProduct(user, product);
            if (existingFavorites.isEmpty()) {
                Favorites favorites = new Favorites();
                favorites.setProduct(product);
                favorites.setUser(user);
                favoritesRepository.save(favorites);
            } else {
                throw new Exception("Product already exists in Favorites");
            }
        } else throw new Exception("Couldn't save products to favorites");
    }

    public void removeFromFavorites(Favorites favoriteToRemove) throws Exception{
        Optional<Favorites> existingFavorites = favoritesRepository.findById(favoriteToRemove.getFavoritesId());
        if (existingFavorites.isPresent()){
            favoritesRepository.delete(existingFavorites.get());
        }  else {
            throw new Exception("Favorite entry not found");
        }
    }
    public void deleteAllFavorites(UserEntity user){
        List<Favorites> userFaves = favoritesRepository.findByUser(user);
        favoritesRepository.deleteAll();
    }

}
