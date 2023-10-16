package com.groceriespriceguide.repository;

import com.groceriespriceguide.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



public interface UserRepository extends CrudRepository<UserEntity, Long> {

    // interface abstraction
    UserEntity findByUsernameAndPassword(String username, String password);
}
