package spotify.server.music.song;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SongManagerTest {

    @InjectMocks
    private SongManager songManager;

    private final AudioFormat audioFormat =
            new AudioFormat(new AudioFormat.Encoding("PCM_SIGNED"), 48000.0f, 16, 1, 2, 48000.0f, false);

    @BeforeEach
    void setUp() {
        songManager = mock(SongManager.class);
    }

    @Test
    void testSearchSongs() {
        Song mockedSong1 = new Song("Test Song 1", "Test Artist 1", audioFormat);
        Song mockedSong2 = new Song("Test Song 2", "Test Artist 2", audioFormat);
        List<Song> mockedAvailableSongs = Arrays.asList(mockedSong1, mockedSong2);

        when(songManager.searchSongs("Test")).thenReturn(mockedAvailableSongs);

        SongManager songManager = new SongManager();
        songManager.getAvailableSongs().add(mockedSong1);
        songManager.getAvailableSongs().add(mockedSong2);

        List<Song> result = songManager.searchSongs("Test");

        assertEquals(2, result.size());
        assertIterableEquals(mockedAvailableSongs, result);
    }

    @Test
    void testTop() {
        Song mockedSong1 = new Song("Test Song 1", "Test Artist 1", audioFormat);
        Song mockedSong2 = new Song("Test Song 2", "Test Artist 2", audioFormat);
        Song mockedSong3 = new Song("Test Song 3", "Test Artist 3", audioFormat);
        List<Song> mockedAvailableSongs = Arrays.asList(mockedSong2, mockedSong3);
        when(songManager.top(2)).thenReturn(mockedAvailableSongs);

        SongManager songManager = new SongManager();
        final int firstSongPlayingNow = 2;
        final int secondSongPlayingNow = 4;
        final int thirdSongPlayingNow = 3;
        songManager.getSongsPlayingNow().put(mockedSong1, firstSongPlayingNow);
        songManager.getSongsPlayingNow().put(mockedSong2, secondSongPlayingNow);
        songManager.getSongsPlayingNow().put(mockedSong3, thirdSongPlayingNow);

        List<Song> result = songManager.top(2);
        assertEquals(2, result.size());
        assertIterableEquals(mockedAvailableSongs, result);
    }

    @Test
    void testFindSong() {
        Song mockedSong1 = new Song("Zombie 1", "Test Artist 1", audioFormat);
        Song mockedSong2 = new Song("Zombie 2", "Test Artist 3", audioFormat);
        Song mockedSong3 = new Song("Thunderstruck", "Test Artist 2", audioFormat);

        when(songManager.findSong("Zombie 2")).thenReturn(mockedSong2);

        SongManager songManager = new SongManager();
        songManager.getAvailableSongs().add(mockedSong1);
        songManager.getAvailableSongs().add(mockedSong2);
        songManager.getAvailableSongs().add(mockedSong3);
        Song result = songManager.findSong("Zombie 2");

        assertEquals(result, mockedSong2);
    }

}
