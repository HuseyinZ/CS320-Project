package org.example.Controller;

import org.example.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    private AuthController authController;

    @BeforeEach
    public void setUp() {
        authController = new AuthController();
    }

    /**
     * Test for T-SRS-RO-001: Checking the sign-in process
     */
    @Test
    public void testLogin_ValidCredentials() {
        // This test assumes there's a valid user in the database with these credentials
        boolean result = authController.login("testuser", "password123");
        assertTrue(result, "Login should succeed with valid credentials");
    }

    /**
     * Test for T-SRS-RO-001: Checking the sign-in process (invalid credentials)
     */
    @Test
    public void testLogin_InvalidCredentials() {
        boolean result = authController.login("nonexistentuser", "wrongpassword");
        assertFalse(result, "Login should fail with invalid credentials");
    }

    /**
     * Test for T-SRS-RO-001.1: Checking the sign-out process
     */
    @Test
    public void testLogout() {
        // First login to have a user to log out
        authController.login("testuser", "password123");

        // Then logout
        authController.logout();

        // Verify the user is logged out
        assertFalse(authController.isLoggedIn(), "User should be logged out after calling logout");
    }

    /**
     * Test for T-SRS-RO-012: Check if the user can create an account
     */
    @Test
    public void testRegister_NewUser() {
        boolean result = authController.register(
                "newuser", "password123", "John Doe",
                "john@example.com", "123 Main St", "1990-01-01"
        );

        assertTrue(result, "Registration should succeed with new user information");
    }

    /**
     * Test for T-SRS-RO-012.1: RO shouldn't let the user create an account with someone else's username
     */
    @Test
    public void testRegister_ExistingUsername() {
        // Assuming "existinguser" already exists in the system
        boolean result = authController.register(
                "existinguser", "password123", "John Doe",
                "john@example.com", "123 Main St", "1990-01-01"
        );

        assertFalse(result, "Registration should fail with existing username");
    }

    /**
     * Test for T-SRS-RO-012.1: RO shouldn't let the user create an account with someone else's email
     */
    @Test
    public void testRegister_ExistingEmail() {
        // Assuming "existing@example.com" already exists in the system
        boolean result = authController.register(
                "newusername", "password123", "John Doe",
                "existing@example.com", "123 Main St", "1990-01-01"
        );

        assertFalse(result, "Registration should fail with existing email");
    }

    /**
     * Test for Admin check functionality
     */
    @Test
    public void testIsAdmin() {
        // First login as an admin user
        authController.login("adminuser", "adminpass");

        boolean result = authController.isAdmin();
        assertTrue(result, "Admin user should be recognized as admin");

        // Logout
        authController.logout();

        // Login as regular user
        authController.login("regularuser", "userpass");

        result = authController.isAdmin();
        assertFalse(result, "Regular user should not be recognized as admin");
    }

    /**
     * Test for getting current user
     */
    @Test
    public void testGetCurrentUser() {
        // Login first
        authController.login("testuser", "password123");

        User currentUser = authController.getCurrentUser();

        assertNotNull(currentUser, "Current user should not be null after login");
        assertEquals("testuser", currentUser.getUsername(), "Username should match the logged in user");
    }

    /**
     * Test for checking login status
     */
    @Test
    public void testIsLoggedIn() {
        // Initially should not be logged in
        assertFalse(authController.isLoggedIn(), "Should not be logged in initially");

        // Login
        authController.login("testuser", "password123");

        // Should now be logged in
        assertTrue(authController.isLoggedIn(), "Should be logged in after successful login");

        // Logout
        authController.logout();

        // Should no longer be logged in
        assertFalse(authController.isLoggedIn(), "Should not be logged in after logout");
    }
}
