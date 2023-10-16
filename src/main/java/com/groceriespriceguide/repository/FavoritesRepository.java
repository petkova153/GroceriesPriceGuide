package com.groceriespriceguide.repository;

import com.groceriespriceguide.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {


}
