package spotify.server.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.user.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class RegisterCommandTest {

    @Mock
    private UserManager mockedUserManager;

    @Mock
    private SpotifyManager mockedSpotifyManager;

    @Mock
    private Client mockedClient;

    @InjectMocks
    private RegisterCommand registerCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockedSpotifyManager.getUserManager()).thenReturn(mockedUserManager);
        when(mockedClient.getEmail()).thenReturn("test@example.com");
    }

    @Test
    void testCallSuccessfullyRegistersUser() {
        String[] args = {"register", "test@example.com", "password"};
        registerCommand = new RegisterCommand(args, mockedClient, mockedSpotifyManager);

        when(mockedUserManager.isUserLoggedIn("test@example.com")).thenReturn(false);
        when(mockedUserManager.registerUser("test@example.com", "password"))
                .thenReturn("Registration successful.");

        String result = registerCommand.call();

        assertEquals("Registration successful.", result);
    }

    @Test
    void testCallUserAlreadyLoggedIn() {
        String[] args = {"register", "test@example.com", "password"};
        registerCommand = new RegisterCommand(args, mockedClient, mockedSpotifyManager);

        when(mockedSpotifyManager.getUserManager()).thenReturn(mockedUserManager);
        when(mockedClient.getEmail()).thenReturn("test@example.com");
        when(mockedUserManager.isUserLoggedIn("test@example.com")).thenReturn(true);

        String result = registerCommand.call();

        assertEquals("You are already logged in.", result);
    }

    @Test
    void testCallInvalidEmail() {
        String[] args = {"invalid_email.", "password"};
        registerCommand = new RegisterCommand(args, mockedClient, mockedSpotifyManager);
        when(mockedClient.getEmail()).thenReturn("test@example.com");
        assertThrows(IllegalArgumentException.class, registerCommand::call);
    }

}