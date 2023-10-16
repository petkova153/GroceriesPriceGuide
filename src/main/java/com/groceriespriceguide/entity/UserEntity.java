package com.groceriespriceguide.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

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
    private String password;
    private String email;
    private String city;
    private Timestamp lastUpdated;
    private Timestamp createdAt;
    private Timestamp lastLoggedIn;
//    @ManyToMany(mappedBy = "favoredBy")
//    private Set<Favorites> favorites;

}
