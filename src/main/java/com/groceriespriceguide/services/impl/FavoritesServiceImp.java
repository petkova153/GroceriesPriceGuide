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

import java.util.List;

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
    //Have to add functionality to check if a product has already been added to favorites.

    public void addToFaves(Long productID, Long userID) throws Exception {
        Product product = productService.findProductById(productID);

        UserEntity user=userService.getUserById(userID);

        if (product != null && user != null){
            Favorites favorites = new Favorites();
            favorites.setProduct(product);

            favoritesRepository.save(favorites);
        } else throw new Exception("Couldn't save products to favorites");


    }

}
