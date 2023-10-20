package com.groceriespriceguide.repository;

import com.groceriespriceguide.entity.Favorites;
import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    List<Favorites>findByUser(UserEntity user);
    Favorites findByUserAndProduct(UserEntity user, Product product);


}
