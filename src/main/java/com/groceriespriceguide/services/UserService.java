package com.groceriespriceguide.services;

import com.groceriespriceguide.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void createUser(UserEntity userEntity) throws Exception;

    void deleteUser(String username, String city) throws Exception;

    UserEntity verifyUser(String username, String password);

    UserEntity getUserById(long userId);

}
