package com.groceriespriceguide.services.impl;

import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.repository.UserRepository;
import com.groceriespriceguide.security.PasswordEncoder;
import com.groceriespriceguide.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring, please provide us copies of the dependencies
    public UserServiceImpl(final UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(UserEntity userEntity) {
            UserEntity checkIfUsernameExists = userRepository.findByUsername(userEntity.getUsername());
            // 1. if username is found to be taken
            if (checkIfUsernameExists != null)
                throw new RuntimeException("USERNAME is taken");
            System.out.println("Sorry, the username is taken, try another one"); // this does not print
            // 2. if email is found to be taken
            UserEntity checkIfEmailExists = userRepository.findByEmail(userEntity.getEmail());
            if (checkIfEmailExists != null) throw new RuntimeException("EMAIL is taken");
            System.out.println("Sorry, the email is taken, try another email"); // this does not print
            this.userRepository.save(userEntity);
            System.out.println(userEntity);
        }

    public UserEntity verifyUser(String username, String plainPassword) {
        UserEntity user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(plainPassword, user.getPassword())) {
            return user; // Passwords match, user is verified
        }

        return null; // User not found or password doesn't match
    }

    public List<UserEntity> getAllUsers() {
        return (ArrayList<UserEntity>)
                this.userRepository.findAll();
    }

    public UserEntity getUserById(long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("User not found for ID: " + userId);
        }
    }
}

//  controls the registering,
//  login In,
//  viewing Profile,
//  viewing favorites,
//  adding favorites.
