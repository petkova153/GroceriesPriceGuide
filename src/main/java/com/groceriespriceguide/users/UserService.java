package com.groceriespriceguide.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class UserService {

    private UserRepository userRepository;

    @Autowired // Spring, please provide us copies of the dependencies
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserEntity userEntity) throws Exception {
        this.userRepository.save(userEntity);
    }

    public UserEntity verifyUser(String username, String password) throws Exception {
        UserEntity user = this.userRepository.findByUsernameAndPassword(username, password);
        System.out.println(user);
        return user;
    }

    public List<UserEntity> getAllUsers() {
        return (ArrayList<UserEntity>)
                this.userRepository.findAll();
    }

    public UserEntity getUserById(long userId) throws Exception {
        return this.userRepository.findById(userId).orElseThrow();
    }
}

//  controls the registering,
//  login In,
//  viewing Profile,
//  viewing favorites,
//  adding favorites.
