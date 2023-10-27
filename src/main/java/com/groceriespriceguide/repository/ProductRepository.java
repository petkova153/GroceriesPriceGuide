package com.groceriespriceguide.repository;

import com.groceriespriceguide.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @NotNull List<Product> findAll();
    Optional <Product> findByProductID(Long id);
    Product findByProductUrl(String productUrl);

    //For Searching
    List<Product> findAll(Specification<Product> specification);

}
