package com.groceriespriceguide.products.services;

import com.groceriespriceguide.products.entity.Product;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public interface ProductService {

    void persistProduct(List<Product> dataList);

    List<Product> getProductData();
}
