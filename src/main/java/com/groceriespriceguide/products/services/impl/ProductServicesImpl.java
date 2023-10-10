package com.groceriespriceguide.products.services.impl;

import com.groceriespriceguide.products.entity.Product;
import com.groceriespriceguide.products.repository.ProductRepository;
import com.groceriespriceguide.products.services.ProductService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        //System.out.println(dataList);
        for (Product product : dataList) {
            entityManager.persist(product);
        }
    }

    //@Override
    public List<Product> getProductData() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) throws Exception{
        for (Product product:productRepository.findAll()){
            if (product.getId().equals(id))
                return productRepository.findByProductID(id).get();
        }
        throw new Exception("Product cannot be found");
    }

    public Product findProductByName(String name){
        return productRepository.findByProductName(name).get(0);
    }

    public Optional getProductByURL(String productURL){
        return productRepository.findByProductUrl(productURL);
    }

    public List<Product> searchProductByKeywordInCertainCategoryAndStore(String keywrd, String category, String store1, String store2, String store3){
        return productRepository.findByKeywordInSpecificCategoryAndStores(keywrd,category,store1,store2,store3);
    }

    public List<Product> getProductsInCategory(String category){
        return productRepository.findProductByProductCategory(category);
    }

}
