package com.groceriespriceguide.repository;

import com.groceriespriceguide.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByUsername(String username); // to query database table

    UserEntity findByEmail(String email);
}
