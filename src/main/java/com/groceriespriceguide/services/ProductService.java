package com.groceriespriceguide.services;

import com.groceriespriceguide.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface ProductService {
    @Transactional
    void persistProduct(List<Product> dataList);

    List<Product> getProductData();
    Product findProductById(Long id)throws Exception;
     Product getProductByURL(String productURL);
        Product updateExistingProduct(Product newProduct);

    List<Product> searchProducts(String keyword, List<String> stores, List<String> categories);

    List<Product> getSortedProducts(List<Product> products, String sortBy);

}
