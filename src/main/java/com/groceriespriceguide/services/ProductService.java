package com.groceriespriceguide.services;

import com.groceriespriceguide.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public interface ProductService {
    @Transactional
    public void persistProduct(List<Product> dataList);

    List<Product> getProductData();
}
