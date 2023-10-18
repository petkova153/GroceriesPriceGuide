package com.groceriespriceguide.controller;

import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.users.UserLoginRequest;
import com.groceriespriceguide.services.impl.UserServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    // collects user information,
    // checks for validity
    // sends and retrieves service information
    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String displayLoginPage() {
        return "login"; //logIn is the name of the file
    }

    @GetMapping("/register")
    public String displayRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleUserRegistration(UserEntity userEntity) {
        try {
            this.userService.createUser(userEntity);
            return "redirect:/login?status=REGISTRATION_COMPLETED";
        } catch (Exception exception) {
            return "redirect:/register?status=REGISTRATION_FAILED&error="
                    + exception.getMessage();
        }
    }

    @PostMapping("/login")
    public String handleUserLogin(UserLoginRequest userLoginRequest,
                                  HttpServletResponse response) {
        try {
            UserEntity user = this.userService.verifyUser(userLoginRequest.getUsername(),
                    userLoginRequest.getPassword());
            if (user == null) throw new Exception
                    ("Please try again, your login details did not match");
            // creates a cookie and saves user id for the session
            Cookie cookie = new Cookie("loggedInUserId", user.getId().toString());
            cookie.setMaxAge(200_000);
            response.addCookie(cookie);
            return "redirect:/favorites";
        } catch (Exception exception) {
            return "redirect:/login?status=LOGIN_FAILED&error=" + exception.getMessage();
        }
    }

    @GetMapping("/logout")
    public String handleLogout(
            @CookieValue(value = "loggedInUserId", defaultValue = "")
            String userId, HttpServletResponse response) {
        Cookie cookie = new Cookie("loggedInUserId", null);
        cookie.setMaxAge(0); // this deleted the cookie
        response.addCookie(cookie);
        return "redirect:/login?status=LOGOUT_SUCCESS";
    }
}
