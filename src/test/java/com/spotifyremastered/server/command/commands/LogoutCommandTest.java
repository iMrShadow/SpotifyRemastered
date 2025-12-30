package com.spotifyremastered.server.command.commands;

import com.spotifyremastered.server.Client;
import com.spotifyremastered.server.SpotifyManager;
import com.spotifyremastered.server.user.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LogoutCommandTest {

    @Mock
    private Client mockClient;

    @Mock
    private SpotifyManager mockSpotifyManager;

    @Mock
    private UserManager mockUserManager;

    @InjectMocks
    private LogoutCommand logoutCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogoutCommand() throws Exception {
        String username = "user@example.com";
        String[] args = {"logout"};

        logoutCommand = new LogoutCommand(args, mockClient, mockSpotifyManager);
        when(mockClient.getEmail()).thenReturn(username);
        when(mockUserManager.isUserLoggedIn("user@example.com")).thenReturn(true);
        when(mockSpotifyManager.getUserManager()).thenReturn(mockUserManager);
        when(mockUserManager.logoutUser(username)).thenReturn("Successfully logged out.");

        String result = logoutCommand.call();

        assertEquals("Successfully logged out.", result);
    }
}
