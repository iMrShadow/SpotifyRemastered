package spotify.server.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.user.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class LoginCommandTest {

    @Mock
    private UserManager mockedUserManager;

    @Mock
    private SpotifyManager mockedSpotifyManager;

    @Mock
    private Client mockedClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockedSpotifyManager.getUserManager()).thenReturn(mockedUserManager);
        when(mockedClient.getEmail()).thenReturn("test@example.com");
    }

    @Test
    void testValidLogin() throws Exception {
        String[] args = {"login", "user@example.com", "password"};
        Client client = new Client(null, null);
        LoginCommand loginCommand = LoginCommand.of(args, client, mockedSpotifyManager);

        when(mockedSpotifyManager.getUserManager().loginUser("user@example.com", "password"))
                .thenReturn("User logged in successfully.");

        String result = loginCommand.call();

        assertEquals("User logged in successfully.", result);
        assertEquals("user@example.com", client.getEmail());
    }

    @Test
    void testInvalidEmailFormat() {
        String[] args = {"login", "invalid_email", "password"};
        Client client = new Client(null, null);
        LoginCommand loginCommand = LoginCommand.of(args, client, mockedSpotifyManager);

        assertThrows(IllegalArgumentException.class, loginCommand::call);
        verifyNoInteractions(mockedSpotifyManager.getUserManager());
    }

    @Test
    void testInvalidArguments() {
        String[] args = {"login", "user@example.com"};
        Client client = new Client(null, null);
        LoginCommand loginCommand = LoginCommand.of(args, client, mockedSpotifyManager);

        assertThrows(IllegalArgumentException.class, loginCommand::call);
        verifyNoInteractions(mockedSpotifyManager.getUserManager());
    }
}
