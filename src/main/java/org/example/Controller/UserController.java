package org.example.Controller;

import org.example.Model.User;
import org.example.Service.UserService;

import java.util.List;

public class UserController {
    private UserService userService;

    public UserController() {
        userService = new UserService();
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public boolean changeAdminStatus(User user, int newStatus) {
        return userService.changeAdminStatus(user, newStatus);
    }

    public boolean deleteUser(int userId) {
        return userService.deleteUser(userId);
    }


}
