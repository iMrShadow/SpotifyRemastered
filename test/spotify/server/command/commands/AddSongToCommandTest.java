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
import spotify.server.music.song.Song;
import spotify.server.music.song.SongManager;
import spotify.server.user.UserManager;

import javax.sound.sampled.AudioFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddSongToCommandTest {

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

    @Mock
    private PlaylistManager mockedPlaylistManager;

    @InjectMocks
    private AddSongToCommand addSongToCommand;

    private final AudioFormat audioFormat =
            new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"), 48000.0f, 16, 1, 2, 48000.0f, false);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSongToCommandWithValidArguments() throws Exception {
        String[] args = {"add-song-to", "\"MyPlaylist\"", "MySong"};
        String username = "user@example.com";

        when(mockedClient.getEmail()).thenReturn(username);
        when(mockedSpotifyManager.getUserManager()).thenReturn(mockedUserManager);
        when(mockedSpotifyManager.getMusicManager()).thenReturn(mockedMusicManager);
        when(mockedMusicManager.getSongManager()).thenReturn(mockedSongManager);
        when(mockedMusicManager.getPlayListManager()).thenReturn(mockedPlaylistManager);

        Song mockedSong = new Song("MySong", "1", audioFormat);

        when(mockedMusicManager.getPlayListManager()).thenReturn(mockedPlaylistManager);
        when(mockedUserManager.isUserLoggedIn(username)).thenReturn(true);

        when(mockedSongManager.findSong("\"MySong\"")).thenReturn(mockedSong);

        when(mockedPlaylistManager.addSongToPlaylist(username, "\"MyPlaylist\"", mockedSong))
                .thenReturn("Song added to playlist");

        addSongToCommand = new AddSongToCommand(args, mockedClient, mockedSpotifyManager);
        String result = addSongToCommand.call();

        verify(mockedMusicManager).getPlayListManager();
        verify(mockedSongManager).findSong("\"MySong\"");
        verify(mockedPlaylistManager).addSongToPlaylist(username, "\"MyPlaylist\"", mockedSong);

        assertEquals("Song added to playlist", result);
    }

    @Test
    void testAddSongToCommandWithInvalidArguments() {
        String[] args = {"add-song-to", "test"};
        String username = "user@example.com";
        String expectedErrorMessage = "Invalid arguments: \"test\". Example: \"add-song-to <playlistName> <songName>\"";
        addSongToCommand = new AddSongToCommand(args, mockedClient, mockedSpotifyManager);

        when(mockedSpotifyManager.getUserManager()).thenReturn(mockedUserManager);
        when(mockedClient.getEmail()).thenReturn(username);
        when(mockedUserManager.isUserLoggedIn(username)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> addSongToCommand.call());

        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
