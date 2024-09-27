package spotify.server.command.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.command.Command;
import spotify.server.command.commands.AddSongToCommand;
import spotify.server.command.commands.CreatePlaylistCommand;
import spotify.server.command.commands.HelpCommand;
import spotify.server.command.commands.LoginCommand;
import spotify.server.command.commands.LogoutCommand;
import spotify.server.command.commands.PlayCommand;
import spotify.server.command.commands.RegisterCommand;
import spotify.server.command.commands.SearchCommand;
import spotify.server.command.commands.ShowPlaylistCommand;
import spotify.server.command.commands.StopCommand;
import spotify.server.command.commands.TopCommand;
import spotify.server.command.commands.UnregisterCommand;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        assertTrue(result instanceof HelpCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateRegisterCommand() {
        Command result = CommandFactory.create("register username password", mockClient, mockSpotifyManager);

        assertTrue(result instanceof RegisterCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateUnregisterCommand() {
        Command result = CommandFactory.create("unregister", mockClient, mockSpotifyManager);

        assertTrue(result instanceof UnregisterCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateLoginCommand() {
        Command result = CommandFactory.create("login username password", mockClient, mockSpotifyManager);

        assertTrue(result instanceof LoginCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateLogoutCommand() {
        Command result = CommandFactory.create("logout", mockClient, mockSpotifyManager);

        assertTrue(result instanceof LogoutCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateSearchCommand() {
        Command result = CommandFactory.create("search query", mockClient, mockSpotifyManager);

        assertTrue(result instanceof SearchCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateTopCommand() {
        Command result = CommandFactory.create("top", mockClient, mockSpotifyManager);

        assertTrue(result instanceof TopCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateCreatePlaylistCommand() {
        Command result = CommandFactory
                .create("create-playlist playlistName", mockClient, mockSpotifyManager);

        assertTrue(result instanceof CreatePlaylistCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateAddSongToCommand() {
        Command result = CommandFactory
                .create("add-song-to playlistName songName", mockClient, mockSpotifyManager);

        assertTrue(result instanceof AddSongToCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateShowPlaylistCommand() {
        Command result = CommandFactory
                .create("show-playlist playlistName", mockClient, mockSpotifyManager);

        assertTrue(result instanceof ShowPlaylistCommand);
        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreatePlayCommand() {
        Command result = CommandFactory
                .create("play songName", mockClient, mockSpotifyManager);

        assertTrue(result instanceof PlayCommand);

        verifyNoInteractions(mockClient, mockSpotifyManager);
    }

    @Test
    void testCreateStopCommand() {
        Command result = CommandFactory
                .create("stop", mockClient, mockSpotifyManager);

        assertTrue(result instanceof StopCommand);
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
