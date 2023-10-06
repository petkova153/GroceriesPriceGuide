package com.groceriespriceguide.products.services.impl;

import com.groceriespriceguide.products.entity.Product;
import com.groceriespriceguide.products.repository.ProductRepository;
import com.groceriespriceguide.products.services.ProductService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServicesImpl implements ProductService {
    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    public ProductServicesImpl(final EntityManager entityManager, final ProductRepository productRepository)
    {
        this.entityManager = entityManager;
        this.productRepository = productRepository;
    }

    //@Override
    @Transactional
    public void persistProduct(final List<Product> dataList) {
        dataList.forEach(entityManager::merge);
    }

    //@Override
    public List<Product> getProductData() {
        return productRepository.findAll();
    }
}
