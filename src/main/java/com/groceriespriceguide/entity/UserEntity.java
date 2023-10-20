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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    @Column(unique = true) // could not execute statement [Duplicate entry '...' for key 'user_entity
    private String username;
    private String password;
    private String city;
    private Timestamp lastUpdated;
    private Timestamp createdAt;
    private Timestamp lastLoggedIn;
    @Column(unique = true) // could not execute statement [Duplicate entry '...' for key 'user_entity
    private String email;

//    @ManyToMany(mappedBy = "favoredBy")
//    private Set<Favorites> favorites;

}
