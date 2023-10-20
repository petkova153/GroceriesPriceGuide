package com.groceriespriceguide.services;

import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavoriteService {
    List<Favorites> getFavoriteProductsForUser(UserEntity user);
    Favorites findByUserAndProduct(UserEntity user, Product product);
    Favorites addToFaves(UserEntity user, Product product) throws Exception;
    void removeFromFavorites(Favorites favoriteToRemove) throws Exception;
    void deleteAllFavorites(UserEntity user);
}
