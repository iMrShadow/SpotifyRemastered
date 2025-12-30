package com.spotifyremastered.server.command.factory;

import com.spotifyremastered.server.Client;
import com.spotifyremastered.server.SpotifyManager;
import com.spotifyremastered.server.command.Command;
import com.spotifyremastered.server.command.commands.AddSongToCommand;
import com.spotifyremastered.server.command.commands.CreatePlaylistCommand;
import com.spotifyremastered.server.command.commands.HelpCommand;
import com.spotifyremastered.server.command.commands.LoginCommand;
import com.spotifyremastered.server.command.commands.LogoutCommand;
import com.spotifyremastered.server.command.commands.PlayCommand;
import com.spotifyremastered.server.command.commands.RegisterCommand;
import com.spotifyremastered.server.command.commands.SearchCommand;
import com.spotifyremastered.server.command.commands.ShowPlaylistCommand;
import com.spotifyremastered.server.command.commands.StopCommand;
import com.spotifyremastered.server.command.commands.TopCommand;
import com.spotifyremastered.server.command.commands.UnregisterCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

class CommandFactoryTest {

    @Mock
    private Client mockClient;

    @Mock
    private SpotifyManager mockSpotifyManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateHelpCommand() {
        Command result = CommandFactory.create("help", mockClient, mockSpotifyManager);

        assertInstanceOf(HelpCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateRegisterCommand() {
        Command result = CommandFactory.create("register username password", mockClient, mockSpotifyManager);

        assertInstanceOf(RegisterCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateUnregisterCommand() {
        Command result = CommandFactory.create("unregister", mockClient, mockSpotifyManager);

        assertInstanceOf(UnregisterCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateLoginCommand() {
        Command result = CommandFactory.create("login username password", mockClient, mockSpotifyManager);

        assertInstanceOf(LoginCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateLogoutCommand() {
        Command result = CommandFactory.create("logout", mockClient, mockSpotifyManager);

        assertInstanceOf(LogoutCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateSearchCommand() {
        Command result = CommandFactory.create("search query", mockClient, mockSpotifyManager);

        assertInstanceOf(SearchCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateTopCommand() {
        Command result = CommandFactory.create("top", mockClient, mockSpotifyManager);

        assertInstanceOf(TopCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateCreatePlaylistCommand() {
        Command result = CommandFactory
                .create("create-playlist playlistName", mockClient, mockSpotifyManager);

        assertInstanceOf(CreatePlaylistCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateAddSongToCommand() {
        Command result = CommandFactory
                .create("add-song-to playlistName songName", mockClient, mockSpotifyManager);

        assertInstanceOf(AddSongToCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateShowPlaylistCommand() {
        Command result = CommandFactory
                .create("show-playlist playlistName", mockClient, mockSpotifyManager);

        assertInstanceOf(ShowPlaylistCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreatePlayCommand() {
        Command result = CommandFactory
                .create("play songName", mockClient, mockSpotifyManager);

        assertInstanceOf(PlayCommand.class, result);

        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateStopCommand() {
        Command result = CommandFactory
                .create("stop", mockClient, mockSpotifyManager);

        assertInstanceOf(StopCommand.class, result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateInvalidCommand() {
        Command result = CommandFactory
                .create("invalid-command", mockClient, mockSpotifyManager);

        assertNull(result);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateNullInput() {
        assertThrows(IllegalArgumentException.class, () ->
                CommandFactory.create(null, mockClient, mockSpotifyManager));
    }

    @Test
    void testCreateBlankInput() {
        assertThrows(IllegalArgumentException.class, () ->
                CommandFactory.create("    ", mockClient, mockSpotifyManager));
    }
}
