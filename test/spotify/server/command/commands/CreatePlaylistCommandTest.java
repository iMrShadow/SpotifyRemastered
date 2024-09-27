package spotify.server.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.music.MusicManager;
import spotify.server.music.playlist.PlaylistManager;
import spotify.server.user.UserManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CreatePlaylistCommandTest {

    @Mock
    private Client mockClient;

    @Mock
    private SpotifyManager mockSpotifyManager;

    @Mock
    private PlaylistManager mockPlaylistManager;

    @Mock
    private UserManager mockUserManager;

    @Mock
    private MusicManager mockMusicManager;

    @InjectMocks
    private CreatePlaylistCommand createPlaylistCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createPlaylistCommand = new CreatePlaylistCommand(
                new String[]{"create-playlist", "playlistName"}, mockClient, mockSpotifyManager);

        when(mockSpotifyManager.getMusicManager()).thenReturn(mockMusicManager);
        when(mockMusicManager.getPlayListManager()).thenReturn(mockPlaylistManager);
        when(mockSpotifyManager.getUserManager()).thenReturn(mockUserManager);
        when(mockClient.getEmail()).thenReturn("user@example.com");
    }

    @Test
    void testCallUserNotLoggedIn() throws Exception {
        when(mockSpotifyManager.getUserManager().isUserLoggedIn(anyString())).thenReturn(false);

        String result = createPlaylistCommand.call();

        assertEquals("User is not logged in.", result);
    }

    @Test
    void testCallValidCreatePlaylist() throws Exception {
        when(mockSpotifyManager.getUserManager().isUserLoggedIn(anyString())).thenReturn(true);
        when(mockPlaylistManager.createPlaylist(anyString(), anyString())).thenReturn("Playlist created successfully.");

        String result = createPlaylistCommand.call();

        assertEquals("Playlist created successfully.", result);
    }

    @Test
    void testCallInvalidArgs() {
        CreatePlaylistCommand invalidArgsCommand = new CreatePlaylistCommand(
                new String[]{"create-playlist"}, mockClient, mockSpotifyManager);

        when(mockSpotifyManager.getUserManager().isUserLoggedIn(anyString())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, invalidArgsCommand::call);
        assertEquals("Invalid arguments: \"\". Example: \"create-playlist <playlistName>\"",
                exception.getMessage());
    }
}
