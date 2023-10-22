package com.groceriespriceguide.services;

import com.groceriespriceguide.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    @Transactional
    public void persistProduct(List<Product> dataList);

    List<Product> getProductData();
    Product findProductById(Long id)throws Exception;
    Product findProductByName(String name);
    Product getProductByURL(String productURL);
    List<Product> searchProductByKeywordInCertainCategoryAndStore(String keywrd, String category, String store1, String store2, String store3);
    List<Product> getProductsInCategory(String category);
    Product updateExistingProduct(Product newProduct);
    String getCreationTimestamp(Product product);
    String getLastUpdatedTimestamp(Product product);
    List<Product> findProductsByStore(String store);
    ////
    List<Product> searchProducts(String keyword, List<String> stores, List<String> categories);

    List<Product> getSortedProducts(List<Product> products, String sortBy);

}
