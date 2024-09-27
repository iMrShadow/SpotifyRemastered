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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class TopCommandTest {

    @Mock
    private Client mockedClient;

    @Mock
    private SpotifyManager mockedSpotifyManager;

    @Mock
    private UserManager mockedUserManager;

    @Mock
    private SongManager mockedSongManager;

    @Mock
    private MusicManager mockedMusicManager;

    @InjectMocks
    private TopCommand topCommand;

    private static final int TOP_SONGS_NUMBER = 5;

    private final AudioFormat audioFormat =
            new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"), 48000.0f, 16, 1, 2, 48000.0f, false);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockedSpotifyManager.getUserManager()).thenReturn(mockedUserManager);
        when(mockedSpotifyManager.getMusicManager()).thenReturn(mockedMusicManager);
        when(mockedMusicManager.getSongManager()).thenReturn(mockedSongManager);
        when(mockedClient.getEmail()).thenReturn("user@example.com");
    }

    @Test
    void testTopCommandWithValidArguments() throws Exception {
        String[] args = {"top", "5"};
        List<Song> topSongs = Arrays.asList(
                new Song("Song1", "1", audioFormat),
                new Song("Song2", "2", audioFormat),
                new Song("Song3", "3", audioFormat)
        );

        when(mockedSongManager.top(TOP_SONGS_NUMBER)).thenReturn(topSongs);
        when(mockedSpotifyManager.getUserManager().isUserLoggedIn("user@example.com")).thenReturn(true);

        topCommand = new TopCommand(args, mockedClient, mockedSpotifyManager);
        String result = topCommand.call();

        assertEquals("Top Songs:\n1. \"Song1 - 1\"\n2. \"Song2 - 2\"\n3. \"Song3 - 3\"", result);
    }

    @Test
    void testTopCommandWithInvalidArguments() {
        String[] args = {"top", "invalid", "wat"};
        String expectedErrorMessage = "Invalid arguments: \"invalid wat\". Example: \"top <number>\"";

        when(mockedSpotifyManager.getUserManager().isUserLoggedIn("user@example.com")).thenReturn(true);

        topCommand = new TopCommand(args, mockedClient, mockedSpotifyManager);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> topCommand.call());

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
