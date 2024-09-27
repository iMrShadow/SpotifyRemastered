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
import spotify.server.music.song.SongManager;
import spotify.server.user.UserManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShowPlaylistCommandTest {

    @Mock
    private Client mockClient;

    @Mock
    private SpotifyManager mockSpotifyManager;

    @Mock
    private UserManager mockUserManager;

    @Mock
    private SongManager mockSongManager;

    @Mock
    private MusicManager mockMusicManager;

    @Mock
    private PlaylistManager mockPlaylistManager;

    @InjectMocks
    private ShowPlaylistCommand showPlaylistCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockSpotifyManager.getUserManager()).thenReturn(mockUserManager);
        when(mockSpotifyManager.getMusicManager()).thenReturn(mockMusicManager);
        when(mockMusicManager.getSongManager()).thenReturn(mockSongManager);
        when(mockMusicManager.getPlayListManager()).thenReturn(mockPlaylistManager);
        when(mockClient.getEmail()).thenReturn("test@example.com");
    }

    @Test
    void testShowPlaylistCommandWithValidArguments() throws Exception {
        String[] args = {"show-playlist", "MyPlaylist"};
        String username = "user@example.com";
        List<String> playlist = Arrays.asList("Song1", "Song2", "Song3");
        ShowPlaylistCommand showPlaylistCommand = new ShowPlaylistCommand(args, mockClient, mockSpotifyManager);

        when(mockClient.getEmail()).thenReturn(username);
        when(mockUserManager.isUserLoggedIn(username)).thenReturn(true);
        when(mockPlaylistManager.showPlaylist(username, "MyPlaylist")).thenReturn(playlist);

        String result = showPlaylistCommand.call();

        verify(mockUserManager).isUserLoggedIn(username);
        verify(mockPlaylistManager).showPlaylist(username, "MyPlaylist");

        assertEquals("Playlist: MyPlaylist\n1. Song1\n2. Song2\n3. Song3", result);
    }

    @Test
    void testShowPlaylistCommandWithInvalidArguments() {
        String[] args = {"show-playlist"};
        String username = "user@example.com";
        String expectedErrorMessage = "Invalid arguments: \"\". Example: \"show <playlistName>\"";

        when(mockClient.getEmail()).thenReturn(username);
        when(mockUserManager.isUserLoggedIn(username)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            showPlaylistCommand = new ShowPlaylistCommand(args, mockClient, mockSpotifyManager);
            showPlaylistCommand.call();
        });

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
