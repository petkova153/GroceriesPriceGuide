package com.groceriespriceguide.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "favorites") // Specify the table name explicitly
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long favoritesId;

//    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
//    @JoinColumn(nullable = false, name = "user_id")
//    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
