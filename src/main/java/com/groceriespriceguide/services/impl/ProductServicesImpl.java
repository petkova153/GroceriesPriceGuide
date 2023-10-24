package com.groceriespriceguide.services.impl;

import com.groceriespriceguide.entity.Product;
import com.groceriespriceguide.entity.ProductFiltering;
import com.groceriespriceguide.repository.ProductRepository;
import com.groceriespriceguide.services.ProductService;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
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
            if (product.getProductID().equals(id) && productRepository.findByProductID(id).isPresent())
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
        try {
            return productRepository.save(updatedProduct);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
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




    public List<Product> getSortedProducts(List<Product> products, String sortBy) {
        Comparator<Product> productComparator;
        switch (sortBy) {
            case "name_asc":
                productComparator = Comparator.comparing(Product::getProductName);
                break;
            case "name_desc":
                productComparator = Comparator.comparing(Product::getProductName, Comparator.reverseOrder());
                break;
            case "price_asc":
                productComparator = Comparator.comparing(Product::getProductPrice, Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "price_desc":
                productComparator = Comparator.comparing(Product::getProductPrice, Comparator.nullsLast(Comparator.reverseOrder()));
                break;
            default:
                // Default to sorting by product name ascending
                productComparator = Comparator.comparing(Product::getProductName);
                break;
        }
        // Sort the provided list of products using the chosen comparator
        products.sort(productComparator);
        return products;
    }

}
