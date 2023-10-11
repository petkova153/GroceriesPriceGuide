package com.groceriespriceguide.products.services;

import com.groceriespriceguide.products.entity.Product;
import com.groceriespriceguide.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public interface ProductService {


    @Transactional
    public void persistProduct(List<Product> dataList);

    List<Product> getProductData();


}
