package com.groceriespriceguide.users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class UserEntity {

    // (id,
    // name,
    // city,
    // email,
    // last Loge In,
    // last Updated,
    // Products? maybe this will be some list? but it should be in product class..

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String username;
    private String email;
    private String city;
    private String favouriteProduct1;
    private String favouriteProduct2;
    private String favouriteProduct3;
    private Timestamp lastUpdated;
    private Timestamp createdAt;
    private Timestamp lastLoggedIn;
}
