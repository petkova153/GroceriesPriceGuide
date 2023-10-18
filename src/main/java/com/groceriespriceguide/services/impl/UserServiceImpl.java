package com.groceriespriceguide.services.impl;

import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.repository.UserRepository;
import com.groceriespriceguide.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Spring, please provide us copies of the dependencies
    public UserServiceImpl(final UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public void createUser(UserEntity userEntity) {
        this.userRepository.save(userEntity);
    }

    public UserEntity verifyUser(String username, String password) {
        UserEntity user = this.userRepository.findByUsernameAndPassword(username, password);
        System.out.println(user);
        return user;
    }

    public List<UserEntity> getAllUsers() {
        return (ArrayList<UserEntity>)
                this.userRepository.findAll();
    }

    public UserEntity getUserById(long userId) {
        return this.userRepository.findById(userId).orElseThrow();
    }
}

//  controls the registering,
//  login In,
//  viewing Profile,
//  viewing favorites,
//  adding favorites.