package com.groceriespriceguide.services.impl;

import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.ProductFiltering;
import com.groceriespriceguide.repository.ProductRepository;
import com.groceriespriceguide.services.ProductService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
            if (product != null) entityManager.persist(product);
        }
    }

    @Override
    public List<Product> getProductData() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) throws Exception{
        for (Product product:productRepository.findAll()){
            if (product.getProductID().equals(id))
                return productRepository.findByProductID(id).get();
        }
        throw new Exception("Product cannot be found");
    }

    public Product findProductByName(String name){
        return productRepository.findByProductName(name).get(0);
    }

    public Product getProductByURL(String productURL){
        return productRepository.findByProductUrl(productURL);
    }

    public List<Product> searchProductByKeywordInCertainCategoryAndStore(String keywrd, String category, String store1, String store2, String store3){
        return productRepository.findByKeywordInSpecificCategoryAndStores(keywrd,category,store1,store2,store3);
    }

    public List<Product> getProductsInCategory(String category){
        return productRepository.findProductByProductCategory(category);
    }


    public Product updateExistingProduct(Product updatedProduct){
            return productRepository.save(updatedProduct);

    }

    private String dateFormatter(Timestamp timestamp){
        if (timestamp != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            return dateFormat.format(timestamp);
        }
        return "Date not available.";

    }

    public String getCreationTimestamp(Product product){
        return dateFormatter(product.getCreatedAT());
    }
    public String getLastUpdatedTimestamp(Product product){
        return dateFormatter(product.getLastUpdated());
    }  

    public List<Product> findProductsByStore(String store){
        return productRepository.findProductByStore(store);
    }

   ///////////////// Searching products is so fuuuun//////////////////////////////
    public List<Product> searchProducts(String keyword, List<String> stores, List<String> categories) {
        Specification<Product> spec = ProductFiltering.filterProducts(keyword, stores, categories);
        return productRepository.findAll(spec);
    }


    public List<Product> getSortedProducts(String sortBy) {
        switch (sortBy) {
            case "name_asc":
                return productRepository.findAllByOrderByNameAsc();
            case "name_desc":
                return productRepository.findAllByOrderByNameDesc();
            case "price_asc":
                return productRepository.findAllByOrderByPriceAsc();
            case "price_desc":
                return productRepository.findAllByOrderByPriceDesc();
            default:
                // Default to returning all products if sortBy parameter is invalid
                return productRepository.findAll();
        }
    }

}
