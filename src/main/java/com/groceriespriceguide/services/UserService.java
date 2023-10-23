package com.groceriespriceguide.services;

import com.groceriespriceguide.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface UserService {

    void createUser(UserEntity userEntity) throws Exception;

    UserEntity verifyUser(String username, String password);

    UserEntity getUserById(long userId);
}
