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

        if (checkIfUsernameExists != null) {
            System.out.println("Sorry, the username is taken, try another username");
            throw new RuntimeException("The USERNAME is taken, please try another one ;-)");
        }

        UserEntity checkIfEmailExists = userRepository.findByEmail(userEntity.getEmail());
        if (checkIfEmailExists != null) {
            System.out.println("Sorry, the email is taken, try another email");
            throw new RuntimeException("The EMAIL is taken, please try another one ;-)");
        }
        this.userRepository.save(userEntity);
        System.out.println("New user was created: " + userEntity);
    }

    public void deleteUser(String username, String password) {
        UserEntity userToDelete = userRepository.findByUsername(username); // usernames are unique
        if (userToDelete != null && passwordEncoder.matches(password, userToDelete.getPassword())) {
            this.userRepository.delete(userToDelete);
            System.out.println("User DELETED: " + userToDelete);
        } else {
            throw new RuntimeException("Sorry but the details you provided did not match, please try again.");
        }
    }

    public UserEntity verifyUser(String username, String plainPassword) {
        UserEntity user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(plainPassword, user.getPassword())) {
            return user; // Passwords match, user is verified
        }
        return null; // User not found or password doesn't match
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

