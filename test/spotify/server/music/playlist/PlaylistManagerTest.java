package spotify.server.music.playlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotify.server.exceptions.PlaylistNotFoundException;
import spotify.server.music.song.Song;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaylistManagerTest {

    private static final String TEST_PLAYLISTS_DIRECTORY = "test_playlists/";
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PLAYLIST_NAME = "TestPlaylist";

    private final AudioFormat audioFormat =
            new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"), 48000.0f, 16, 1, 2, 48000.0f, false);


    private PlaylistManager playlistManager;

    @BeforeEach
    void setUp() {
        playlistManager = new PlaylistManager(TEST_PLAYLISTS_DIRECTORY);
    }

    @Test
    void testCreatePlaylistSuccessfully() {
        String result = playlistManager.createPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME);
        assertEquals("Playlist created successfully.", result);

        File playlistFile = new File(TEST_PLAYLISTS_DIRECTORY + File.separator + TEST_USERNAME + ".csv");
        assertTrue(playlistFile.exists());

        playlistFile.delete();
    }

    @Test
    void testCreatePlaylistAlreadyExists() throws IOException {
        File testPlaylistFile = new File(TEST_PLAYLISTS_DIRECTORY + File.separator + TEST_USERNAME + ".csv");
        testPlaylistFile.createNewFile();

        playlistManager.createPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME);
        String result = playlistManager.createPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME);
        assertEquals("Playlist already exists.", result);

        testPlaylistFile.delete();
    }

    @Test
    void testAddSongToPlaylistSuccessfully() throws IOException {
        File testPlaylistFile = new File(TEST_PLAYLISTS_DIRECTORY + File.separator + TEST_USERNAME + ".csv");
        testPlaylistFile.createNewFile();

        Song mockedSong = new Song("Test Song 1", "Test Artist 1", audioFormat);
        playlistManager.createPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME);

        String result = playlistManager.addSongToPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME, mockedSong);
        assertEquals("Song added to playlist.", result);

        testPlaylistFile.delete();
    }

    @Test
    void testAddSongToPlaylistAlreadyExists() throws IOException {
        File testPlaylistFile = new File(TEST_PLAYLISTS_DIRECTORY + File.separator + TEST_USERNAME + ".csv");
        testPlaylistFile.createNewFile();

        Song mockedSong = new Song("Test Song 1", "Test Artist 1", audioFormat);
        playlistManager.createPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME);
        playlistManager.addSongToPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME, mockedSong);

        String result = playlistManager.addSongToPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME, mockedSong);
        assertEquals("Song already exists in the playlist.", result);

        testPlaylistFile.delete();
    }

    @Test
    void testShowPlaylistSuccessfully() throws IOException {
        File testPlaylistFile = new File(TEST_PLAYLISTS_DIRECTORY + File.separator + TEST_USERNAME + ".csv");
        testPlaylistFile.createNewFile();

        Song mockedSong = new Song("Test Song 1", "Test Artist 1", audioFormat);
        playlistManager.createPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME);
        playlistManager.addSongToPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME, mockedSong);

        List<String> playlist = playlistManager.showPlaylist(TEST_USERNAME, TEST_PLAYLIST_NAME);

        assertEquals(1, playlist.size());
        assertTrue(playlist.contains(mockedSong.getSongName()));

        testPlaylistFile.delete();
    }

    @Test
    void testShowPlaylistPlaylistNotFound() {
        assertThrows(PlaylistNotFoundException.class,
                () -> playlistManager.showPlaylist(TEST_USERNAME, "NonExistentPlaylist"));
    }
}