package spotify.server.command.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spotify.server.Client;
import spotify.server.SpotifyManager;
import spotify.server.music.MusicManager;
import spotify.server.music.song.Song;
import spotify.server.music.song.SongManager;
import spotify.server.user.UserManager;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SearchCommandTest {

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

    @InjectMocks
    private SearchCommand searchCommand;

    private final AudioFormat audioFormat =
            new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"), 48000.0f, 16, 1, 2, 48000.0f, false);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockSpotifyManager.getUserManager()).thenReturn(mockUserManager);
        when(mockSpotifyManager.getMusicManager()).thenReturn(mockMusicManager);
        when(mockMusicManager.getSongManager()).thenReturn(mockSongManager);
        when(mockClient.getEmail()).thenReturn("test@example.com");
    }

    @Test
    void testCallUserNotLoggedIn() throws Exception {
        when(mockUserManager.isUserLoggedIn("test@example.com")).thenReturn(false);

        SearchCommand searchCommand = new SearchCommand(new String[]{"search", "AC"}, mockClient, mockSpotifyManager);
        String result = searchCommand.call();

        assertEquals("User is not logged in.", result);
    }

    @Test
    void testCallSearchSongs() throws Exception {
        when(mockUserManager.isUserLoggedIn("test@example.com")).thenReturn(true);

        List<Song> mockedSongs = new ArrayList<>();
        mockedSongs.add(new Song("Song1", "1", audioFormat));
        mockedSongs.add(new Song("Song2", "2", audioFormat));
        when(mockSongManager.searchSongs(anyString())).thenReturn(mockedSongs);

        searchCommand = new SearchCommand(new String[]{"search", "Song"}, mockClient, mockSpotifyManager);
        String result = searchCommand.call();

        String expected = "Top Songs:\n1. \"Song1 - 1\"\n2. \"Song2 - 2\"\n";
        assertEquals(expected, result);
    }

    @Test
    void testCallInvalidArgs() {
        searchCommand = new SearchCommand(new String[]{"search"}, mockClient, mockSpotifyManager);

        when(mockUserManager.isUserLoggedIn("test@example.com")).thenReturn(true);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> searchCommand.call());
        assertEquals("Invalid arguments: \"\". Example: \"search <songName>\"", exception.getMessage());
    }
}
