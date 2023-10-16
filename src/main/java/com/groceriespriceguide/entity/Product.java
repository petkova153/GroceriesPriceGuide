package com.groceriespriceguide.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productID;
    private String productName;
    private Double productPrice;
    private String unitOfMeasure;
    private String store;
    private String productCategory;
    @Column(unique = true)
    private String productUrl;
    private String pictureUrl;
    private Timestamp lastUpdated;
    private Timestamp createdAT;

//    @ManyToMany(mappedBy = "favorites")
//    private Set<UserEntity> users;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
//    private List<Favorites> favoritesList;
}
