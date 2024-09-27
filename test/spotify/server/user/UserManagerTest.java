package spotify.server.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserManagerTest {

    @Mock
    private User mockedUser;

    private UserManager userManager;

    private Map<String, User> users;
    private Map<String, Boolean> usersLoggedIn;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        users = new HashMap<>();
        usersLoggedIn = new HashMap<>();
        userManager = new UserManager(users, usersLoggedIn, UserManager.TEST_USER_DATA_FILE_PATH);
    }

    @Test
    void testGetUsers() {
        when(mockedUser.email()).thenReturn("test@example.com");
        users.put("test@example.com", mockedUser);
        assertEquals(List.of(mockedUser), userManager.getUsers());
    }

    @Test
    void testRegisterUser() {
        when(mockedUser.email()).thenReturn("test@example.com");

        users.remove("test@example.com");
        usersLoggedIn.remove("test@example.com");

        assertTrue(userManager.registerUser("test@example.com", "password").contains("successfully"));
    }

    @Test
    void testUnregisterUser() {
        when(mockedUser.email()).thenReturn("test@example.com");

        users.put("test@example.com", mockedUser);
        usersLoggedIn.put("test@example.com", false);

        assertTrue(userManager.unregisterUser("test@example.com").contains("successfully"));
    }

    @Test
    void testLoginUser() {
        when(mockedUser.email()).thenReturn("test@example.com");
        when(mockedUser.password()).thenReturn("password");

        users.put("test@example.com", mockedUser);
        usersLoggedIn.put("test@example.com", false);

        String result = userManager.loginUser("test@example.com", "password");

        assertTrue(result.contains("Successfully logged in"));
    }

    @Test
    void testLogoutUser() {
        when(mockedUser.email()).thenReturn("test@example.com");

        users.put("test@example.com", mockedUser);
        usersLoggedIn.put("test@example.com", true);

        String result = userManager.logoutUser("test@example.com");

        assertTrue(result.contains("successfully"));
    }

    @Test
    void testReadAllUsers() throws IOException {
        Path tempFilePath = Files.createTempFile("temp_users", ".csv");
        Files.writeString(tempFilePath, "email,password\nuser1,password1\nuser 2,password2");

        userManager.readAllUsers(tempFilePath);

        assertEquals(2, users.size());
        assertTrue(users.containsKey("user1"));
        assertTrue(users.containsKey("user 2"));

        Files.delete(tempFilePath);
    }

    @Test
    void testIsUserLoggedIn() {
        when(mockedUser.email()).thenReturn("test@example.com");
        usersLoggedIn.put("test@example.com", true);

        assertTrue(userManager.isUserLoggedIn("test@example.com"));
    }

    @Test
    void testIsUserRegistered() {
        when(mockedUser.email()).thenReturn("test@example.com");
        users.put("test@example.com", mockedUser);

        assertTrue(userManager.isUserRegistered("test@example.com"));
    }

}

