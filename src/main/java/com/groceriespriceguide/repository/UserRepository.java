package com.groceriespriceguide.repository;

import com.groceriespriceguide.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<UserEntity, Long> {
    // interface abstraction
    UserEntity findByUsernameAndPassword(String username, String password);

    UserEntity findByUsername(String username); // to query database table

    UserEntity findByEmail(String email);
}
