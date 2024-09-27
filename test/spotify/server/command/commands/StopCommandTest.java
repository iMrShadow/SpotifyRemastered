package spotify.server.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;
import spotify.server.user.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class StopCommandTest {

    @Mock
    private Client mockClient;

    @Mock
    private SpotifyManager mockSpotifyManager;

    @Mock
    private UserManager mockUserManager;

    @InjectMocks
    private StopCommand stopCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockSpotifyManager.getUserManager()).thenReturn(mockUserManager);
        when(mockClient.getEmail()).thenReturn("user@example.com");
    }

    @Test
    void testCallUserNotLoggedIn() throws Exception {
        when(mockSpotifyManager.getUserManager().isUserLoggedIn("user@example.com")).thenReturn(false);

        String result = stopCommand.call();

        assertEquals("User is not logged in.", result);
    }

    @Test
    void testCallValidStop() throws Exception {
        when(mockSpotifyManager.getUserManager().isUserLoggedIn("user@example.com")).thenReturn(true);
        Command stopCommand = new StopCommand(new String[]{"stop"}, mockClient, mockSpotifyManager);
        String result = stopCommand.call();

        assertEquals("Stopped playing.", result);
    }

    @Test
    void testCallInvalidArgs() {
        StopCommand invalidArgsCommand = new StopCommand(new String[]{"stop", "test"}, mockClient, mockSpotifyManager);
        when(mockSpotifyManager.getUserManager().isUserLoggedIn("user@example.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, invalidArgsCommand::call);
        assertEquals("Invalid arguments: \"test\". Example: \"stop\"", exception.getMessage());
    }
}
