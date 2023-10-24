package com.groceriespriceguide.repository;

import com.groceriespriceguide.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findByProductName(String productName);
    Optional <Product> findByProductID(Long id);
    Product findByProductUrl(String productUrl);

    //For Searching
    List<Product> findAll(Specification<Product> specification);

}
