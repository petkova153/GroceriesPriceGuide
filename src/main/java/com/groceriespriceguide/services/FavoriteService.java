package com.groceriespriceguide.services;

import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavoriteService {
    List<Favorites> getFavoriteProductsForUser(UserEntity user);
    List<Favorites> findByUserAndProduct(UserEntity user, Product product);
    void addToFaves(Long productID, Long userID) throws Exception;
    void removeFromFavorites(Favorites favoriteToRemove) throws Exception;
    void deleteAllFavorites(UserEntity user);
}
