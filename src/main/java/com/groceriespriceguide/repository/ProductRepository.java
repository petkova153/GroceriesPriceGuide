package com.groceriespriceguide.repository;

import com.groceriespriceguide.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findByProductName(String productName);
    Optional <Product> findByProductID(Long id);
    Product findByProductUrl(String productUrl);

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:keyword% AND (p.productCategory = :category1) AND (p.store = :store1 OR p.store = :store2 OR p.store=:store3)")
    List<Product> findByKeywordInSpecificCategoryAndStores(@Param("keyword") String keyword,
                                                           @Param("category1") String category1,
                                                           @Param("store1") String store1,
                                                           @Param("store2") String store2,
                                                           @Param("store3") String store3);

    List<Product> findProductByProductCategory (String productCategory);
    List<Product> findProductByStore (String store);

    //For Searching
    List<Product> findAll(Specification<Product> specification);

    // For filtering

    @Query("SELECT p FROM Product p ORDER BY p.productName ASC")
    List<Product> findAllByOrderByNameAsc();

    @Query("SELECT p FROM Product p ORDER BY p.productName DESC")
    List<Product> findAllByOrderByNameDesc();

    @Query("SELECT p FROM Product p ORDER BY p.productPrice ASC")
    List<Product> findAllByOrderByPriceAsc();

    @Query("SELECT p FROM Product p ORDER BY p.productPrice DESC")
    List<Product> findAllByOrderByPriceDesc();



}
