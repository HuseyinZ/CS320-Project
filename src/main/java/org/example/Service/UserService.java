package org.example.Service;

import org.example.DAO.UserDAO;
import org.example.Model.User;

public class UserService {

    private final UserDAO userDAO;
    private static User currentUser;

    public UserService() {
        userDAO = new UserDAO();
    }

    public boolean login(String username, String password) {
        boolean isAuthenticated = userDAO.authenticate(username, password);
        if (isAuthenticated) {
            currentUser = userDAO.getUserByUsername(username);
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }

    public boolean registerUser(String username, String password, String name, String email, String address, String dateOfBirth) {

        if (userDAO.isUsernameExists(username) || userDAO.isEmailExists(email)) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setAddress(address);
        newUser.setDateOfBirth(dateOfBirth);
        newUser.setAdmin(false);
        newUser.setBanned(false);

        return userDAO.createUser(newUser);
    }

    public boolean updateProfile(String name, String email, String address, String dateOfBirth) {
        if (!isLoggedIn()) {
            return false;
        }

        User userToUpdate = currentUser;
        userToUpdate.setName(name);
        userToUpdate.setEmail(email);
        userToUpdate.setAddress(address);
        userToUpdate.setDateOfBirth(dateOfBirth);

        boolean updateResult = userDAO.updateUser(userToUpdate);
        if (updateResult) {
            // Update the current user object with new details
            currentUser = userToUpdate;
        }
        return updateResult;
    }

    public boolean banUser(int userId) {
        if (!isAdmin()) {
            return false;
        }

        User userToBan = userDAO.getUserByUsername(String.valueOf(userId));
        if (userToBan == null) {
            return false;
        }

        userToBan.setBanned(true);
        return userDAO.updateUser(userToBan);
    }

    public User updateUser(int userId, String newUsername, String newPassword, String newName, String newEmail, String newAddress, String newDob, boolean isBanned) {
        if (!isAdmin()) {
            return null;
        }

        User userToUpdate = userDAO.getUserById(userId);

        if (userToUpdate == null) {
            return null;
        }

        userToUpdate.setUsername(newUsername);
        userToUpdate.setPassword(newPassword);
        userToUpdate.setName(newName);
        userToUpdate.setEmail(newEmail);
        userToUpdate.setAddress(newAddress);
        userToUpdate.setDateOfBirth(newDob);
        userToUpdate.setBanned(isBanned);

        return userToUpdate;
    }

}
