package com.groceriespriceguide.controller;

import com.groceriespriceguide.entity.UserEntity;
import com.groceriespriceguide.security.PasswordEncoder;
import com.groceriespriceguide.services.FavoriteService;
import com.groceriespriceguide.services.ProductService;
import com.groceriespriceguide.users.UserLoginRequest;
import com.groceriespriceguide.services.impl.UserServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    // collects user information,
    // checks for validity
    // sends and retrieves service information
    final UserServiceImpl userService;
    //  If you're building an application that will always rely on the Spring ecosystem,
    //  @Autowired is a suitable choice. Seeking Portability:
    //  If you're aiming to write code that's not tied to a specific framework or container,
    //  or you're working with Java EE, then @Inject is the way to go.Aug 21, 2023
    final ProductService productService;
    final FavoriteService favoriteService;
    final PasswordEncoder passwordEncoder;

    public UserController(final UserServiceImpl userService, final ProductService productService,
                          final FavoriteService favoriteService, final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.productService = productService;
        this.favoriteService = favoriteService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String indexPage(Model model, @CookieValue(value = "loggedInUserId", defaultValue = "") String userId) {
        if (userId.isEmpty()) {
            System.out.println(userId);
            model.addAttribute("userLogged", "not logged");
        } else {
            System.out.println(userId);
            model.addAttribute("userLogged", "logged");
        }
        return "index";
    }

    @GetMapping("/register")
    public String displayRegistrationPage(@RequestParam(name = "error", required = false) String errorReg, Model model) {
        if (errorReg != null) {
            System.out.println("Registration error message after re-direct in register page: " + errorReg);
            model.addAttribute("failed_registration", errorReg);
        }
        return "register";
    }

    @PostMapping("/register")
    public String handleUserRegistration(UserEntity userEntity, Model model) {
        try {
            String encryptedPassword = passwordEncoder.encode(userEntity.getPassword());
            userEntity.setPassword(encryptedPassword);
            this.userService.createUser(userEntity);
            return "redirect:/login?status=REGISTRATION_COMPLETED";
        } catch (Exception exception) {
            String errorMessageReg = exception.getMessage();
            model.addAttribute("failed_registration", errorMessageReg);
            return "redirect:/register?status=REGISTRATION_FAILED&error=" + errorMessageReg;
        }
    }

    @GetMapping("/login")
    public String displayLoginPage(@RequestParam(name = "error", required = false) String errorLogin, Model model) {
        if (errorLogin != null) {
            System.out.println("The Log in error message after re-direct in login page: " + errorLogin);
            model.addAttribute("failed_login", errorLogin);
        }
        return "login"; // returning html file
    }

    @PostMapping("/login")
    public String handleUserLogin(UserLoginRequest userLoginRequest,
                                  HttpServletResponse response, Model model) {
        try {
            UserEntity user = this.userService.verifyUser(userLoginRequest.getUsername(),
                    userLoginRequest.getPassword());

            if (user == null) {
                String errorMessage = "Your login details did not match, please try again or REGISTER";
                model.addAttribute("failed_login", errorMessage);
                return "redirect:/login?status=LOGIN_FAILED&error=" + errorMessage;
            }
            // Create a cookie and save the user ID for the session
            Cookie cookie = new Cookie("loggedInUserId", user.getId().toString());
            cookie.setMaxAge(172_800); // seconds = 48 hours
            response.addCookie(cookie);

            return "redirect:/favorites";

        } catch (Exception exception) {
            String errorMessage = "Your login details did not match, please try again or REGISTER";
            model.addAttribute("failed_login", errorMessage);
            return "redirect:/login?status=LOGIN_FAILED&error=" + errorMessage;
        }
    }

    @GetMapping("/logout")
    public String handleLogout(@CookieValue(value = "loggedInUserId", defaultValue = "") String id, HttpServletResponse response) {
        Cookie cookie = new Cookie("loggedInUserId", null);
        cookie.setMaxAge(0); // This deletes
        response.addCookie(cookie);
        return "redirect:/login?status=LOGOUT_SUCCESS";
    }

    @GetMapping("/remove_user")
    public String RemoveUser(@RequestParam(name = "error", required = false) String errorUserDelete, Model model) {
        if (errorUserDelete != null) {
            System.out.println("The ERROR message upon an attempt TO DELETE user: " + errorUserDelete);
            model.addAttribute("delete_status", errorUserDelete);
        }
        return "/remove_user";
    }

    @PostMapping("/remove_user")
    public String deleteUser(UserLoginRequest userLoginRequest,
                             @CookieValue(value = "loggedInUserId", defaultValue = "") String id, HttpServletResponse response, Model model) {

        System.out.println("Username received from the client: " + userLoginRequest.getUsername());
        UserEntity verifiedUser = this.userService.verifyUser(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        System.out.println("User verification result (found/null): " + verifiedUser);
        try {
            if (verifiedUser != null) {
                this.favoriteService.deleteAllFavorites(verifiedUser);
            }
            this.userService.deleteUser(userLoginRequest.getUsername(), userLoginRequest.getPassword());
            Cookie cookieRemoval = new Cookie("loggedInUserId", null);
            cookieRemoval.setMaxAge(0);
            response.addCookie(cookieRemoval);
            System.out.println(verifiedUser);
            return "redirect:/products?status=USER_DELETED";

        } catch (Exception e) {
            String errorMessageDel = e.getMessage();
            model.addAttribute("delete_status", errorMessageDel);
            return "redirect:/remove_user?status=COULD_NOT_DELETE&error=" + errorMessageDel;
        }
    }
}
