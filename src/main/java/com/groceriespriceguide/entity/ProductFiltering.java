package com.groceriespriceguide.entity;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductFiltering {

    public static Specification<Product> filterProducts(String keyword, List<String> stores, List<String> categories){
        return ((root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if (keyword!=null && !keyword.isEmpty()){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%"));
            }


            // Filter by stores
            if (stores != null && !stores.isEmpty()) {
                predicates.add(root.get("store").in(stores));
            }

            // Filter by categories
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("productCategory").in(categories));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
