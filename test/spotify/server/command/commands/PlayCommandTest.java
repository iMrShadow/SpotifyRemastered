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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class PlayCommandTest {

    @Mock
    private Client mockClient;

    @Mock
    private SpotifyManager mockSpotifyManager;

    @Mock
    private SongManager mockSongManager;

    @Mock
    private MusicManager musicManager;

    @Mock
    private UserManager mockUserManager;

    @Mock
    private Song mockSong;

    @InjectMocks
    private PlayCommand playCommand;

    private final int availablePort = 1338;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        playCommand = new PlayCommand(new String[]{"play", "songName"}, mockClient, mockSpotifyManager);

        when(mockSpotifyManager.getMusicManager()).thenReturn(musicManager);
        when(mockSpotifyManager.getMusicManager().getSongManager()).thenReturn(mockSongManager);
        when(mockSongManager.findSong(anyString())).thenReturn(mockSong);
        when(mockSpotifyManager.getUserManager()).thenReturn(mockUserManager);
        when(mockClient.getEmail()).thenReturn("user@example.com");

        when(mockSpotifyManager.isPortAvailable(mockClient)).thenReturn(true);
        when(mockSpotifyManager.getNewPort(any())).thenReturn(availablePort);
    }

    @Test
    void testCallValidPlay() throws Exception {
        when(mockSpotifyManager.getUserManager().isUserLoggedIn("user@example.com")).thenReturn(true);

        when(mockSong.getSongName()).thenReturn("songName - 1");
        when(mockSongManager.findSong("songName")).thenReturn(mockSong);
        String audioFormatString = (" PCM_SIGNED" + " 48000.0f" + " 16" + " 1" + " 2" + " 48000.0f" + " false");
        when(mockSpotifyManager.getNewPort(mockClient)).thenReturn(availablePort);
        when(mockSong.getAudioFormatString()).thenReturn(audioFormatString);

        String result = playCommand.call();
        assertEquals("play songName - 1 " + audioFormatString + " " +
                Integer.parseInt(String.valueOf(availablePort)), result);
    }

    @Test
    void testCallInvalidArgs() {
        playCommand = new PlayCommand(new String[]{"play"}, mockClient, mockSpotifyManager);
        when(mockSpotifyManager.getUserManager().isUserLoggedIn("user@example.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, playCommand::call);
        assertEquals("Invalid arguments: \"\". Example: \"play <songName>\"", exception.getMessage());
    }
}