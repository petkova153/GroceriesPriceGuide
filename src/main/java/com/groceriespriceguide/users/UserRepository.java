package com.groceriespriceguide.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    // interface abstraction
    UserEntity findByUsernameAndPassword(String username, String password);
}
