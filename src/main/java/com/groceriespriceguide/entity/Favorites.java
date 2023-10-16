package com.groceriespriceguide.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long favoritesID;
    @OneToOne
    private Product product;
    @OneToOne
    @JoinColumn(name="user_id", unique = true)
    private UserEntity user;


}
