package org.example.Controller;


import org.example.Model.User;
import org.example.Service.UserService;

public class AuthController {
    private UserService userService;

    public AuthController() {
        userService = new UserService();
    }

    public boolean login(String username, String password) {
        return userService.login(username, password);
    }

    public void logout() {
        userService.logout();
    }

    public boolean register(String username, String password, String name,
                            String email, String address, String dateOfBirth) {
        return userService.registerUser(username, password, name, email, address, dateOfBirth);
    }

    public User getCurrentUser() {
        if(userService.getCurrentUser() != null) {
            return userService.getCurrentUser();
        }
        else{
            return new User(-1, "default", "default","default","default","default","1999-12-12", false);
        }
    }

    public boolean isLoggedIn() {
        return userService.isLoggedIn();
    }

    public boolean isAdmin() {
        return userService.isAdmin();
    }
}
