import org.example.Controller.AuthController;
import org.example.Controller.CarController;
import org.example.Controller.ReservationController;
import org.example.Model.Car;
import org.example.Model.Reservation;
import org.example.Model.User;
import org.example.Service.CarService;
import org.example.Service.ReservationService;
import org.example.Service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

/**
 * Test suite for RideOn (RO) system implementing tests defined in the Software Test Plan
 */
public class RideOnTests {
    private AuthController authController;
    private CarController carController;
    private ReservationController reservationController;
    private UserService userService;
    private CarService carService;
    private ReservationService reservationService;

    // Test user credentials
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NAME = "Test User";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_ADDRESS = "123 Test St";
    private static final String TEST_DOB = "1990-01-01";

    // Admin user credentials
    private static final String ADMIN_USERNAME = "utku";
    private static final String ADMIN_PASSWORD = "san00san";

    @Before
    public void setUp() {
        authController = new AuthController();
        carController = new CarController();
        reservationController = new ReservationController();
        userService = new UserService();
        carService = new CarService();
        reservationService = new ReservationService();

        // Ensure test user exists
        if (!authController.login(TEST_USERNAME, TEST_PASSWORD)) {
            authController.register(TEST_USERNAME, TEST_PASSWORD, TEST_NAME, TEST_EMAIL, TEST_ADDRESS, TEST_DOB);
        }
        authController.logout();
    }

    @After
    public void tearDown() {
        // Logout after each test
        if (authController.isLoggedIn()) {
            authController.logout();
        }
    }

    /**
     * Test ID: T-SRS-RO-001
     * Verifies user sign in process (successful and unsuccessful login attempts)
     */
    @Test
    public void testUserSignIn() {
        // Test successful login
        boolean loginResult = authController.login(TEST_USERNAME, TEST_PASSWORD);
        Assert.assertTrue("User should be able to sign in with correct credentials", loginResult);
        Assert.assertTrue("User should be logged in after successful login", authController.isLoggedIn());

        // Logout for next test
        authController.logout();

        // Test unsuccessful login with incorrect password
        loginResult = authController.login(TEST_USERNAME, "wrongpassword");
        Assert.assertFalse("User should not be able to sign in with incorrect password", loginResult);
        Assert.assertFalse("User should not be logged in after failed login", authController.isLoggedIn());

        // Test unsuccessful login with non-existent username
        loginResult = authController.login("nonexistentuser", TEST_PASSWORD);
        Assert.assertFalse("User should not be able to sign in with non-existent username", loginResult);
        Assert.assertFalse("User should not be logged in after failed login", authController.isLoggedIn());
    }

    /**
     * Test ID: T-SRS-RO-001.1
     * Verifies user sign out process
     */
    @Test
    public void testUserSignOut() {
        // First sign in
        authController.login(TEST_USERNAME, TEST_PASSWORD);
        Assert.assertTrue("User should be logged in before testing sign out", authController.isLoggedIn());

        // Test sign out
        authController.logout();
        Assert.assertFalse("User should be logged out after signing out", authController.isLoggedIn());
    }

    /**
     * Test ID: T-SRS-RO-002
     * Verifies car search functionality
     */
    @Test
    public void testCarSearch() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);
        // Add a test car to ensure we have something to search for
        carController.addCar("Toyota", "Corolla", 2022, "Red", 2000, "34ZZ0000", 100000, "z0", "gas", "manual");

        // Test search with positive result
        List<Car> searchResults = carController.searchCars("Toyota");
        Assert.assertFalse("Search should return results for existing car brand", searchResults.isEmpty());

        // Test search with negative result
        searchResults = carController.searchCars("NonExistentBrand");
        Assert.assertTrue("Search should return no results for non-existent car brand", searchResults.isEmpty());
    }

    /**
     * Test ID: T-SRS-RO-003
     * Verifies car reservation functionality
     */
    @Test
    public void testCarReservation() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        // Add a test car
        carController.addCar("honda", "civic", 2023, "blue", 1000, "34ZZ0001", 50000, "z1", "gas", "manual");
        List<Car> cars = carController.searchCars("Honda");
        Car testCar = cars.getFirst();

        // Setup reservation period
        LocalDate startTime = LocalDate.now().plusDays(1);
        LocalDate endTime = startTime.plusDays(3);

        // Test successful reservation
        boolean reserved = reservationController.createReservation(testCar.getCarId(), startTime, endTime);
        Assert.assertTrue("Reservation should be created successfully", reserved);

        // Verify car is now reserved for that period
        // This would need to be tested differently based on the actual implementation
        List<Reservation> userReservations = reservationController.getUserReservations();
        boolean foundReservation = false;
        for (Reservation res : userReservations) {
            if (res.getCarId() == testCar.getCarId()) {
                foundReservation = true;
                break;
            }
        }
        Assert.assertTrue("User should have a reservation for the car", foundReservation);
    }

    /**
     * Test ID: T-SRS-RO-003.1
     * Verifies prevention of double booking
     */
    @Test
    public void testPreventDoubleBooking() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        // Add a test car
        carController.addCar("ford", "focus", 2022, "yellow", 1400, "34ZZ0002", 100000, "z2", "diesel", "manual");
        List<Car> cars = carController.searchCars("ford");
        Car testCar = cars.getFirst();

        // Setup reservation period
        LocalDate startTime = LocalDate.now().plusDays(2);
        LocalDate endTime = startTime.plusDays(4);

        // Make first reservation
        boolean firstReservation = reservationController.createReservation(testCar.getCarId(), startTime, endTime);
        Assert.assertTrue("First reservation should be created successfully", firstReservation);

        // Logout and create a second user
        authController.logout();
        String secondUsername = "testuser2";
        String secondPassword = "password456";
        if (!authController.login(secondUsername, secondPassword)) {
            authController.register(secondUsername, secondPassword, "Test User 2", "test2@example.com", "456 Test St", "1992-02-02");
        }
        authController.login(secondUsername, secondPassword);

        // Attempt to make overlapping reservation
        boolean secondReservation = reservationController.createReservation(testCar.getCarId(), startTime.plusDays(1), endTime.plusDays(1));
        Assert.assertFalse("Second overlapping reservation should not be created", secondReservation);
    }

    /**
     * Test ID: T-SRS-RO-003.3
     * Verifies prevention of multiple simultaneous reservations by same user
     */
    @Test
    public void testPreventMultipleSimultaneousReservations() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        // Add two test cars
        carController.addCar("renault", "clio", 2022, "Red", 1500, "34ZZ0003", 100000, "z3", "gas", "automatic");
        carController.addCar("nissan", "altima", 2021, "pink", 2000, "34ZZ0004", 100000, "z4", "diesel", "manual");

        List<Car> mazdaCars = carController.searchCars("renault");
        List<Car> nissanCars = carController.searchCars("nissan");
        Car testCar1 = mazdaCars.getFirst();
        Car testCar2 = nissanCars.getFirst();

        // Setup reservation period
        LocalDate startTime = LocalDate.now().plusDays(5);
        LocalDate endTime = startTime.plusDays(2);

        // Make first reservation
        boolean firstReservation = reservationController.createReservation(testCar1.getCarId(), startTime, endTime);
        Assert.assertTrue("First reservation should be created successfully", firstReservation);

        // Attempt to make second reservation for same period
        boolean secondReservation = reservationController.createReservation(testCar2.getCarId(), startTime, endTime);
        Assert.assertFalse("User should not be able to make multiple simultaneous reservations", secondReservation);
    }

    /**
     * Test ID: T-SRS-RO-003.4
     * Verifies reservation logging
     */
    @Test
    public void testReservationLogging() {
        // Login first
        authController.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        // Add a test car
        carController.addCar("hyundai", "elantra", 2023, "Red", 2000, "34ZZ0005", 100000, "z5", "gas", "manual");
        List<Car> cars = carController.searchCars("Hyundai");
        Car testCar = cars.getLast();

        // Setup reservation period
        LocalDate startTime = LocalDate.now().plusDays(7);
        LocalDate endTime = startTime.plusDays(3);

        // Make reservation
        boolean reserved = reservationController.createReservation(testCar.getCarId(), startTime, endTime);
        Assert.assertTrue("Reservation should be created successfully", reserved);

        // Verify reservation is logged in database
        List<Reservation> userReservations = reservationController.getUserReservations();
        boolean reservationFound = false;
        int reservationId = -1;
        for (Reservation r : userReservations) {
            if (r.getCarId() == testCar.getCarId()) {
                reservationFound = true;
                reservationId = r.getReservationId();
                break;
            }
        }
        Assert.assertTrue("Reservation should be logged in the database", reservationFound);

        // Cancel reservation
        boolean cancelResult = reservationController.cancelReservation(reservationId);
        Assert.assertTrue("Reservation should be successfully cancelled", cancelResult);

        // Verify reservation is still logged after cancellation
        userReservations = reservationController.getAllReservations();
        reservationFound = false;
        for (Reservation r : userReservations) {
            if (r.getReservationId() == reservationId && r.isCancelled()) {
                reservationFound = true;
                break;
            }
        }
        Assert.assertTrue("Cancelled reservation should still be logged in the database", reservationFound);
    }

    /**
     * Test ID: T-SRS-RO-004
     * Verifies car filtering functionality
     * no database selected??
     */
    @Test
    public void testCarFiltering() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        // Add test cars with different attributes
        carController.addCar("BMW", "x5", 2022, "black", 5000, "34ZZ0006", 100000, "z6", "gas", "manual");
        carController.addCar("BMW", "3 series", 2019, "blue", 4000, "34ZZ0007", 100000, "z7", "gas", "manual");
        carController.addCar("mercedes", "c class", 2023, "gray", 5000, "34ZZ0008", 100000, "z8", "gas", "manual");

        // Test filter by brand
        List<Car> filteredCars = carController.filterCars(null,  "BMW", null,null, null, null, null, null, null, null, null);
        Assert.assertFalse("Filter should return results for existing brand", filteredCars.isEmpty());
        for (Car car : filteredCars) {
            Assert.assertEquals("All filtered cars should have the specified brand", "BMW", car.getBrand());
        }

        // Test filter by color
        filteredCars = carController.filterCars(null, null, null, "black", null, null,null,null,null, null, null);
        Assert.assertFalse("Filter should return results for existing color", filteredCars.isEmpty());
        for (Car car : filteredCars) {
            Assert.assertEquals("All filtered cars should have the specified color", "black", car.getColor());
        }

        // Test filter by year
        filteredCars = carController.filterCars(2023, null, null, null, null, null,null,null,null, null, null);
        Assert.assertFalse("Filter should return results for existing year", filteredCars.isEmpty());
        for (Car car : filteredCars) {
            Assert.assertEquals("All filtered cars should have the specified year", 2023, car.getYear());
        }
    }

    /**
     * Test ID: T-SRS-RO-004.1/2
     * Verifies multiple filter criteria including price range
     * no database selected??
     */
    @Test
    public void testMultipleFilterCriteria() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        // Add test cars with different attributes
        carController.addCar("audi", "a4", 2018, "blue", 4000, "34ZZ0009", 50000, "z9", "gas", "manual");
        carController.addCar("audi", "q5", 2023, "white", 4500, "34ZZ0010", 50000, "z10", "gas", "manual");
        carController.addCar("audi", "a6", 2023, "white", 5000, "34ZZ0011", 50000, "z11", "gas", "manual");


        // Test filter by brand and color
        List<Car> filteredCars = carController.filterCars(null, "audi", null, "white", null,null,null,null, null, null, null);
        Assert.assertFalse("Filter should return results for existing criteria", filteredCars.isEmpty());
        for (Car car : filteredCars) {
            Assert.assertEquals("All filtered cars should have the specified brand", "audi", car.getBrand());
            Assert.assertEquals("All filtered cars should have the specified color", "white", car.getColor());
        }

        // Test filter by price range
        filteredCars = carController.filterCars(null, null, null, null, 3900.0, 4700.0, null,null,null, null,null);
        Assert.assertFalse("Filter should return results for price range", filteredCars.isEmpty());
        for (Car car : filteredCars) {
            Assert.assertTrue("All filtered cars should be within price range",
                    car.getPricePerDay() >= 80.0 && car.getPricePerDay() <= 120.0);
        }
    }

    /**
     * Test ID: T-SRS-RO-008
     * Verifies user profile update functionality
     * ??
     */
    @Test
    public void testUserProfileUpdate() {

        authController.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        User currentUser = authController.getCurrentUser();
        User updatedUser = userService.updateUser(
                currentUser.getUserId(),
                TEST_USERNAME,
                TEST_PASSWORD,
                "Updated Name",
                "updated@example.com",
                "456 Updated St",
                TEST_DOB,
                false
        );

        Assert.assertNotNull("User profile should be updated successfully", updatedUser);
        Assert.assertEquals("User name should be updated", "Updated Name", updatedUser.getName());
        Assert.assertEquals("User email should be updated", "updated@example.com", updatedUser.getEmail());
        Assert.assertEquals("User address should be updated", "456 Updated St", updatedUser.getAddress());

        // Verify changes persist by logging out and back in
        authController.logout();
        authController.login(TEST_USERNAME, TEST_PASSWORD);
        User reloadedUser = authController.getCurrentUser();

        Assert.assertEquals("User name should persist after re-login", "Updated Name", reloadedUser.getName());
        Assert.assertEquals("User email should persist after re-login", "updated@example.com", reloadedUser.getEmail());
    }

    /**
     * Test ID: T-SRS-RO-009
     * Verifies viewing of user reservations
     */
    @Test
    public void testViewUserReservations() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        // Add a test car
        carController.addCar("subaru", "outback", 2022, "green", 2500, "34ZZ0012", 50000, "z12", "gas", "manual");
        List<Car> cars = carController.searchCars("subaru");
        Car testCar = cars.getFirst();

        // Make a reservation
        LocalDate startTime = LocalDate.now().plusDays(10);
        LocalDate endTime = startTime.plusDays(2);
        boolean reserved = reservationController.createReservation(testCar.getCarId(), startTime, endTime);
        Assert.assertTrue("Reservation should be created successfully", reserved);

        // Retrieve user reservations
        List<Reservation> userReservations = reservationController.getUserReservations();

        Assert.assertFalse("User should have at least one reservation", userReservations.isEmpty());
        boolean reservationFound = false;
        for (Reservation r : userReservations) {
            if (r.getCarId() == testCar.getCarId()) {
                reservationFound = true;
                break;
            }
        }
        Assert.assertTrue("Created reservation should be in the user's reservations list", reservationFound);
    }

    /**
     * Test ID: T-SRS-RO-010
     * Verifies modifying and cancelling reservations
     */
    @Test
    public void testModifyAndCancelReservation() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        // Add a test car
        carController.addCar("volkswagen", "golf", 2018, "brown", 1900, "34ZZ0013", 50000, "z13", "gas", "manual");
        List<Car> cars = carController.searchCars("volkswagen");
        Car testCar = cars.getLast();

        // Make a reservation
        LocalDate startTime = LocalDate.now().plusDays(12);
        LocalDate endTime = startTime.plusDays(3);
        boolean reserved = reservationController.createReservation(testCar.getCarId(), startTime, endTime);
        Assert.assertTrue("Reservation should be created successfully", reserved);

        // Get the reservation ID
        List<Reservation> userReservations = reservationController.getUserReservations();
        int reservationId = -1;
        for (Reservation r : userReservations) {
            if (r.getCarId() == testCar.getCarId()) {
                reservationId = r.getReservationId();
                break;
            }
        }
        Assert.assertTrue("Reservation ID should be found", reservationId != -1);

        // Modify reservation
        LocalDate newStartTime = startTime.plusDays(1);
        LocalDate newEndTime = newStartTime.plusDays(4);
        boolean modified = reservationController.modifyReservation(
                reservationId, testCar.getCarId(), newStartTime, newEndTime);

        Assert.assertTrue("Reservation should be modified successfully", modified);

        // Cancel reservation
        boolean cancelResult = reservationController.cancelReservation(reservationId);
        Assert.assertTrue("Reservation should be successfully cancelled", cancelResult);

        // Verify reservation is canceled
        userReservations = reservationController.getUserReservations();
        boolean foundCancelled = false;
        for (Reservation r : userReservations) {
            if (r.getReservationId() == reservationId && r.isCancelled()) {
                foundCancelled = true;
                break;
            }
        }
        Assert.assertTrue("Reservation should be marked as cancelled", foundCancelled);
    }

    /**
     * Test ID: T-SRS-RO-011 and T-SRS-RO-011.1
     * Verifies accessing all cars list
     */
    @Test
    public void testAccessAllCars() {
        // Login first
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        // Add some test cars
        carController.addCar("kia", "sportage", 2022, "blue", 4000, "34ZZ0014", 50000, "z14", "gas", "automatic");
        carController.addCar("honda", "accord", 2018, "blue", 1000, "34ZZ0015", 50000, "z15", "gas", "manual");

        // Get all cars list
        long startTime = System.currentTimeMillis();
        List<Car> allCars = carController.getAllCars();
        long endTime = System.currentTimeMillis();

        Assert.assertFalse("All cars list should not be empty", allCars.isEmpty());

        // Check response time for T-SRS-RO-011.1
        long responseTime = endTime - startTime;
        Assert.assertTrue("All cars should be retrieved within 2 seconds", responseTime < 2000);
    }

    /**
     * Test ID: T-SRS-RO-012
     * Verifies user registration
     */
    @Test
    public void testUserRegistration() {
        // Generate unique username for test
        String newUsername = "newuser" + System.currentTimeMillis();

        // Register new user
        boolean registered = authController.register(
                newUsername,
                "newpassword123",
                "New User",
                "new@example.com",
                "789 New St",
                "1995-05-05"
        );

        Assert.assertTrue("New user should be registered successfully", registered);

        // Verify login works for new user
        authController.logout();
        boolean loginResult = authController.login(newUsername, "newpassword123");
        Assert.assertTrue("New user should be able to login", loginResult);
    }

    /**
     * Test ID: T-SRS-RO-012.1
     * Verifies prevention of duplicate usernames and emails
     */
    @Test
    public void testPreventDuplicateUsernames() {
        // First ensure our test user exists
        authController.logout();
        if (!authController.login(TEST_USERNAME, TEST_PASSWORD)) {
            authController.register(TEST_USERNAME, TEST_PASSWORD, TEST_NAME, TEST_EMAIL, TEST_ADDRESS, TEST_DOB);
        }
        authController.logout();

        // Try to register user with same username
        boolean registered = authController.register(
                TEST_USERNAME,
                "diffpassword",
                "Different Name",
                "different@example.com",
                "Different Address",
                "1991-01-01"
        );

        Assert.assertFalse("User with duplicate username should not be registered", registered);

        // Try to register user with same email
        registered = authController.register(
                "different_username",
                "diffpassword",
                "Different Name",
                TEST_EMAIL,
                "Different Address",
                "1991-01-01"
        );

        Assert.assertFalse("User with duplicate email should not be registered", registered);
    }

    /**
     * Test ID: T-SRS-RO-006
     * Verifies admin can change car availability
     */
    @Test
    public void testAdminChangeCarAvailability() {
        // Login as admin
        boolean adminLogin = authController.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        Assert.assertTrue("Admin should be able to login", adminLogin);

        // Add a test car
        carController.addCar("lexus", "rx", 2022, "silver", 1000, "34ZZ0016", 50000, "z16", "gas", "manual");
        List<Car> cars = carController.searchCars("lexus");
        Car testCar = cars.getLast();

        // Change car availability
        boolean availabilityChanged = carController.updateCarAvailability(testCar.getCarId(), "maintenance", LocalDate.now(), LocalDate.now().plusDays(2));
        Assert.assertTrue("Admin should be able to change car availability", availabilityChanged);

        // Verify car availability is changed
        Car updatedCar = carController.getCarDetails(testCar.getCarId());
        Assert.assertFalse("Car availability should be updated", updatedCar.isAvailable(LocalDate.now().plusDays(1)));
    }

    /**
     * Test ID: T-SRS-RO-007
     * Verifies admin can delete a reservation
     */
    @Test
    public void testAdminDeleteReservation() {
        // First create a user and make a reservation
        authController.login(TEST_USERNAME, TEST_PASSWORD);
        carController.addCar("chevrolet", "malibu", 2022, "blue", 1000, "34ZZ0017", 50000, "z17", "gas", "manual");
        List<Car> cars = carController.searchCars("Chevrolet");
        Car testCar = cars.getFirst();

        LocalDate startTime = LocalDate.now().plusDays(15);
        LocalDate endTime = startTime.plusDays(2);
        reservationController.createReservation(testCar.getCarId(), startTime, endTime);

        // Get the reservation ID
        List<Reservation> userReservations = reservationController.getUserReservations();
        int reservationId = userReservations.getFirst().getReservationId();

        // Now login as admin
        authController.logout();
        authController.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        // Since the controller doesn't have a direct method for admin deletion,
        // we'll use the service level method
        boolean deleteResult = reservationService.deleteReservation(reservationId);
        Assert.assertTrue("Admin should be able to delete reservation", deleteResult);

        // Log back in as user to verify reservation is deleted
        authController.logout();
        authController.login(TEST_USERNAME, TEST_PASSWORD);

        List<Reservation> updatedReservations = reservationController.getUserReservations();
        boolean reservationStillExists = false;
        for (Reservation r : updatedReservations) {
            if (r.getReservationId() == reservationId) {
                reservationStillExists = true;
                break;
            }
        }
        Assert.assertFalse("Reservation should be deleted", reservationStillExists);
    }

    /**
     * Test ID: T-SRS-RO-008 (Admin version)
     * Verifies admin can search and update database
     */
    @Test
    public void testAdminDatabaseManagement() {
        // Login as admin
        authController.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        // Add a new car
        boolean carAdded = carController.addCar("acura", "tlx", 2022, "blue", 1000, "34ZZ0017", 50000, "z17", "gas", "manual");
        Assert.assertTrue("Admin should be able to add new car", carAdded);

        // Search for the car
        List<Car> searchResults = carController.searchCars("Acura");
        Assert.assertFalse("Admin should be able to search for cars", searchResults.isEmpty());
        Car foundCar = searchResults.getFirst();

        // Update car details
        boolean updated = carController.updateCar(
                foundCar.getCarId(),
                "acura",
                "mdx",
                2023,
                "black",
                2000
        );

        Assert.assertTrue("Admin should be able to update car details", updated);

        // Verify the updates
        Car updatedCar = carController.getCarDetails(foundCar.getCarId());
        Assert.assertEquals("Car model should be updated", "mdx", updatedCar.getModel());
        Assert.assertEquals("Car color should be updated", "black", updatedCar.getColor());
        Assert.assertEquals("Car price should be updated", 2000, updatedCar.getPricePerDay(), 0.001);
    }
}