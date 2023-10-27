package com.groceriespriceguide.services.impl;

import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.repository.FavoritesRepository;
import com.groceriespriceguide.services.FavoriteService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FavoritesServiceImp implements FavoriteService {



    private final FavoritesRepository favoritesRepository;

    public FavoritesServiceImp( final FavoritesRepository favoritesRepository)
    {
        this.favoritesRepository= favoritesRepository;
    }

    public List<Favorites> getFavoriteProductsForUser(UserEntity user){
        return favoritesRepository.findByUser(user);
    }
    public Favorites findByUserAndProduct(UserEntity user, Product product){
        return favoritesRepository.findByUserAndProduct(user, product);
    }


    public void addToFaves(UserEntity user, Product product) throws Exception {
        if (product != null && user != null) {
            Favorites existingFavorites = favoritesRepository.findByUserAndProduct(user, product);
            if (existingFavorites == null) {
                // Product doesn't exist in favorites, so we can create a new entry
                Favorites favorites = new Favorites();
                favorites.setProduct(product);
                favorites.setUser(user);
                favoritesRepository.save(favorites);
            } else {
                throw new Exception("Product already exists in Favorites");
            }
        } else {
            throw new Exception("Couldn't save products to favorites");
        }
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
        favoritesRepository.deleteAll(userFaves);
    }

}
