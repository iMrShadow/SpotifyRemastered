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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UnregisterCommandTest {

    @Mock
    private Client mockClient;

    @Mock
    private SpotifyManager mockSpotifyManager;

    @Mock
    private UserManager mockUserManager;

    @InjectMocks
    private UnregisterCommand unregisterCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockSpotifyManager.getUserManager()).thenReturn(mockUserManager);
        when(mockClient.getEmail()).thenReturn("user@example.com");
    }

    @Test
    void testCallUserNotLoggedIn() {
        unregisterCommand = new UnregisterCommand(new String[]{"unregister"}, mockClient, mockSpotifyManager);
        when(mockUserManager.isUserLoggedIn("user@example.com")).thenReturn(false);

        String result = unregisterCommand.call();

        assertEquals("User is not logged in.", result);
    }

    @Test
    void testCallValidUnregister() {
        when(mockClient.getEmail()).thenReturn("user@example.com");
        when(mockUserManager.isUserLoggedIn("user@example.com")).thenReturn(true);
        when(mockUserManager.unregisterUser("user@example.com")).thenReturn("User unregistered successfully.");
        doNothing().when(mockClient).setUserToGuest();
        unregisterCommand = new UnregisterCommand(new String[]{"unregister"}, mockClient, mockSpotifyManager);

        String result = unregisterCommand.call();

        assertEquals("User unregistered successfully.", result);
    }

    @Test
    void testCallInvalidArgs() {
        unregisterCommand = new UnregisterCommand(
                new String[]{"unregister", "test"}, mockClient, mockSpotifyManager);

        when(mockUserManager.isUserLoggedIn("user@example.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> unregisterCommand.call());
        assertEquals("Invalid arguments: \"" + "test" + "\". Example: \"unregister\"", exception.getMessage());
    }
}
